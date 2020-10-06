package com.bitapp.presentation.ui.utils

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat

object Formatter {
    /**
     * Generates and returns a multiple spanned string based on the arguments passed
     *
     * @param context Context
     * @param mainString The main string to which the spanned strings are added
     * @param spanWords [mainString] arguments of type [SpanWord]
     *
     * @return [SpannableStringBuilder]
     */
    fun buildMultipleSpannedString(context: Context, mainString: String, vararg spanWords: SpanWord): SpannableStringBuilder {
        val subStringStartCountList = ArrayList<Int>()
        val subStringList = ArrayList<String>()
        var subStringLength = 0

        for (i in spanWords.indices) {
            val word = spanWords[i]
            val argStringPos = "%${i + 1}$"
            if (i == 0) {
                /*Here -4 is done to deducted for the space of "% value $ type"
                for each argument string, we have to do it */
                subStringLength += word.string.length - 4
                val count = mainString.indexOf(argStringPos)
                subStringStartCountList.add(count)
            } else {
                val count = (mainString.indexOf(argStringPos) + subStringLength)
                subStringStartCountList.add(count)
                subStringLength += word.string.length - 4
            }
            subStringList.add(word.string)
        }

        val varargsArray: Array<Any> = Array(subStringList.size) {}
        subStringList.toArray()?.copyInto(varargsArray)

        val formattedString = SpannableStringBuilder(String.format(mainString, *varargsArray))

        for (i in spanWords.indices) {
            val word = spanWords[i]
            val endCount = word.string.length
            val startCount = subStringStartCountList[i]
            if (word.color != 0) {
                formattedString.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, word.color)), startCount, (startCount + endCount), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            if (word.isBold && word.isItalic) {
                formattedString.setSpan(StyleSpan(Typeface.BOLD_ITALIC), startCount, (startCount + endCount), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            } else {
                if (word.isBold) {
                    formattedString.setSpan(StyleSpan(Typeface.BOLD), startCount, (startCount + endCount), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                if (word.isItalic) {
                    formattedString.setSpan(StyleSpan(Typeface.ITALIC), startCount, (startCount + endCount), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
        }
        return formattedString
    }
}

/**
 * Model class for generating spanned string
 *
 * @property string string to be spanned
 * @property isBold whether span type includes bold
 * @property isItalic whether span type includes Italic
 * @property shouldColor whether or not to color the string
 * @property color color to be used, 0 if [shouldColor] is false.
 *
 * @author Rolbin
 */
data class SpanWord(
        @NonNull val string: String,
        val isBold: Boolean = false,
        val isItalic: Boolean = false,
        @Nullable val color: Int = 0
)