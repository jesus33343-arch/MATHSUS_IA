package com.example.mathsus_ia.ui.methods

import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import io.noties.markwon.Markwon
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin

/**
 * Markwon instance configured with JLaTeXMath, shared by any screen that needs to render
 * a function expression as real math typography instead of raw "x^2"/"log(x,10)" text.
 */
@Composable
fun rememberMathMarkwon(textSize: TextUnit = 16.sp): Markwon {
    val context = LocalContext.current
    val textSizePx = with(LocalDensity.current) { textSize.toPx() }
    return remember(context, textSizePx) {
        Markwon.builder(context)
            .usePlugin(MarkwonInlineParserPlugin.create())
            .usePlugin(
                JLatexMathPlugin.create(textSizePx) { builder ->
                    builder.inlinesEnabled(true)
                }
            )
            .build()
    }
}

/** Renders LaTeX (wrapped in $$...$$) via Markwon, since Compose's Text() can't typeset math. */
@Composable
fun LatexText(
    latex: String,
    color: Color,
    markwon: Markwon,
    modifier: Modifier = Modifier,
    textSizeSp: Float = 16f
) {
    val colorArgb = color.toArgb()
    AndroidView(
        modifier = modifier,
        factory = { context ->
            TextView(context).apply {
                textSize = textSizeSp
                setTextColor(colorArgb)
            }
        },
        update = { textView ->
            textView.setTextColor(colorArgb)
            markwon.setMarkdown(textView, latex)
        }
    )
}

private val FUNCTION_NAMES = listOf("sin", "cos", "tan", "cot", "sec", "csc", "ln", "log")

/**
 * Best-effort conversion from the app's mXparser-style function syntax (e.g. "2*sin(x)+log(x,10)-3*x^2+pi")
 * to LaTeX. Not a full parser — covers the syntax actually used across MATHSUS (the operators
 * +,-,*,/,^, the trig/log/sqrt functions, and the pi/e constants).
 */
fun functionExprToLatex(expr: String): String {
    var s = expr.trim()
    if (s.isEmpty()) return ""

    s = wrapSqrt(s)

    // Two-argument log(x, base) -> \log_{base}(x)
    s = Regex("""log\(\s*([^,()]+)\s*,\s*([^()]+)\)""").replace(s) { m ->
        "\\log_{${m.groupValues[2].trim()}}(${m.groupValues[1].trim()})"
    }

    FUNCTION_NAMES.forEach { name ->
        s = Regex("""\b$name\(""").replace(s) { "\\$name(" }
    }

    // Exponents: wrap in braces so multi-digit/negative exponents render correctly.
    s = Regex("""\^\(([^()]+)\)""").replace(s) { m -> "^{${m.groupValues[1]}}" }
    s = Regex("""\^(-?[a-zA-Z0-9.]+)""").replace(s) { m -> "^{${m.groupValues[1]}}" }

    s = Regex("""\bpi\b""").replace(s) { "\\pi" }
    s = s.replace("*", " \\cdot ")

    return s
}

private fun wrapSqrt(input: String): String {
    val marker = "sqrt("
    val builder = StringBuilder()
    var i = 0
    while (i < input.length) {
        if (input.startsWith(marker, i)) {
            val argStart = i + marker.length
            var depth = 1
            var j = argStart
            while (j < input.length && depth > 0) {
                when (input[j]) {
                    '(' -> depth++
                    ')' -> depth--
                }
                if (depth > 0) j++
            }
            val inner = input.substring(argStart, j.coerceAtMost(input.length))
            builder.append("\\sqrt{").append(wrapSqrt(inner)).append("}")
            i = j + 1
        } else {
            builder.append(input[i])
            i++
        }
    }
    return builder.toString()
}
