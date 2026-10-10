package com.nexcut.ai

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.graphics.drawable.GradientDrawable

class MainActivity : Activity() {
    private val bg = Color.rgb(13, 14, 19)
    private val panel = Color.rgb(25, 27, 36)
    private val muted = Color.rgb(164, 168, 185)
    private val purple = Color.rgb(151, 93, 255)
    private val cyan = Color.rgb(93, 218, 255)
    private var selectedVideo: Uri? = null
    private lateinit var status: TextView
    private lateinit var prompt: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()

        val scroll = ScrollView(this)
        scroll.setBackgroundColor(bg)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20), dp(22), dp(20), dp(28))
        }
        scroll.addView(layout)
        setContentView(scroll)

        val brandRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        val logo = TextView(this).apply {
            text = "N"
            textSize = 22f
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
            setTextColor(Color.WHITE)
            background = rounded(purple, 16)
        }
        brandRow.addView(logo, LinearLayout.LayoutParams(dp(46), dp(46)))
        val brandText = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(12), 0, 0, 0)
        }
        brandText.addView(text("NEXCUT AI", 20f, Color.WHITE, true))
        brandText.addView(text("YOUR PERSONAL AI EDITOR", 10f, cyan, true))
        brandRow.addView(brandText, LinearLayout.LayoutParams(0, -2, 1f))
        val badge = text("BETA", 11f, cyan, true)
        badge.background = rounded(Color.rgb(27, 50, 62), 20)
        badge.setPadding(dp(12), dp(7), dp(12), dp(7))
        brandRow.addView(badge)
        layout.addView(brandRow)

        val hero = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(22), dp(18), dp(22))
            background = rounded(Color.rgb(36, 28, 58), 22)
        }
        val glow = text("✦  AI VIDEO WORKSPACE", 11f, cyan, true)
        hero.addView(glow)
        val headline = text("خلّينا نصنع\nشيئًا مميزًا.", 29f, Color.WHITE, true)
        headline.setPadding(0, dp(12), 0, dp(8))
        hero.addView(headline)
        hero.addView(text("مونتاج بطريقتك — بدون إضافة موسيقى تلقائيًا.", 14f, Color.rgb(218, 216, 230), false))
        layout.addView(hero, marginTop(22))

        layout.addView(text("ابدأ مشروعك", 20f, Color.WHITE, true), marginTop(26))
        layout.addView(text("اختر الطريقة التي تناسبك", 13f, muted, false), marginTop(5))

        val autoCard = actionCard("✦", "المونتاج التلقائي", "اختر فيديو وابدأ إعداد خطة مونتاج", purple)
        autoCard.setOnClickListener { pickVideo("auto") }
        layout.addView(autoCard, marginTop(14))

        val chatCard = actionCard("⌘", "المونتاج بالأوامر", "اكتب ما تريد تغييره في الفيديو", cyan)
        chatCard.setOnClickListener { showChatEditor() }
        layout.addView(chatCard, marginTop(12))

        layout.addView(text("استوديو الأدوات", 20f, Color.WHITE, true), marginTop(26))
        val toolsRow = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        toolsRow.addView(smallTool("🎨", "ألوان سينمائية"), LinearLayout.LayoutParams(0, dp(94), 1f))
        val spacer = View(this)
        toolsRow.addView(spacer, LinearLayout.LayoutParams(dp(10), 1))
        toolsRow.addView(smallTool("✂", "قصّ الفيديو"), LinearLayout.LayoutParams(0, dp(94), 1f))
        toolsRow.addView(View(this), LinearLayout.LayoutParams(dp(10), 1))
        toolsRow.addView(smallTool("☼", "الإضاءة"), LinearLayout.LayoutParams(0, dp(94), 1f))
        layout.addView(toolsRow, marginTop(12))

        status = text("لم يتم اختيار فيديو بعد", 13f, muted, false)
        status.setPadding(dp(14), dp(13), dp(14), dp(13))
        status.background = rounded(panel, 14)
        layout.addView(status, marginTop(20))

        layout.addView(text("ملاحظة النسخة التجريبية", 12f, cyan, true), marginTop(18))
        layout.addView(text("هذه نسخة أولية للواجهة واختيار الفيديو. ربط نموذج ذكاء اصطناعي فعلي ومحرك تنفيذ المونتاج وتصدير الفيديو يحتاج إلى تطوير واختبار إضافيين.", 12f, muted, false), marginTop(6))
    }

    private fun showChatEditor() {
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16), dp(16), dp(16), dp(16))
            background = rounded(panel, 18)
        }
        box.addView(text("اكتب وصف المونتاج", 18f, Color.WHITE, true))
        prompt = EditText(this).apply {
            hint = "مثال: أريد ألوانًا باردة وانتقالات ناعمة..."
            setHintTextColor(muted)
            setTextColor(Color.WHITE)
            textSize = 14f
            minLines = 3
            gravity = Gravity.TOP
            setPadding(dp(12), dp(12), dp(12), dp(12))
            background = rounded(Color.rgb(39, 41, 52), 12)
        }
        box.addView(prompt, marginTop(12))
        val pick = button("اختيار فيديو", purple)
        pick.setOnClickListener { pickVideo("chat") }
        box.addView(pick, marginTop(12))
        val plan = button("تجهيز طلب المونتاج", Color.rgb(55, 48, 78))
        plan.setOnClickListener {
            val p = prompt.text.toString().trim()
            if (p.isEmpty()) {
                Toast.makeText(this, "اكتب وصف التعديل أولًا", Toast.LENGTH_SHORT).show()
            } else {
                status.text = "تم حفظ وصفك محليًا: $p\nلم يتم تنفيذ التعديلات بعد؛ يلزم ربط نموذج AI ومحرك مونتاج."
                Toast.makeText(this, "تم تسجيل الوصف في هذه الجلسة", Toast.LENGTH_LONG).show()
            }
        }
        box.addView(plan, marginTop(8))
        val dialog = android.app.AlertDialog.Builder(this)
            .setView(box)
            .setNegativeButton("إغلاق", null)
            .create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun pickVideo(mode: String) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "video/*"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        pendingMode = mode
        startActivityForResult(intent, 101)
    }

    private var pendingMode = "auto"

    @Deprecated("Deprecated in Android API, retained for broad device compatibility")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_OK) {
            selectedVideo = data?.data
            if (selectedVideo != null) {
                status.text = "تم اختيار الفيديو بنجاح.\nالخطوة التالية: ربط تحليل AI وتنفيذ التعديلات."
                Toast.makeText(this, "تم اختيار الفيديو", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun actionCard(icon: String, title: String, subtitle: String, accent: Int): LinearLayout {
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(16), dp(17), dp(16), dp(17))
            background = rounded(panel, 18)
        }
        val iconView = TextView(this).apply {
            text = icon
            textSize = 24f
            gravity = Gravity.CENTER
            setTextColor(accent)
            background = rounded(Color.rgb(43, 39, 59), 14)
        }
        card.addView(iconView, LinearLayout.LayoutParams(dp(52), dp(52)))
        val textCol = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(13), 0, dp(8), 0)
        }
        textCol.addView(text(title, 16f, Color.WHITE, true))
        textCol.addView(text(subtitle, 12f, muted, false).apply { setPadding(0, dp(5), 0, 0) })
        card.addView(textCol, LinearLayout.LayoutParams(0, -2, 1f))
        card.addView(text("›", 28f, accent, true))
        return card
    }

    private fun smallTool(icon: String, label: String): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            background = rounded(panel, 16)
            addView(text(icon, 23f, cyan, true))
            addView(text(label, 11f, Color.WHITE, false).apply { setPadding(0, dp(7), 0, 0) })
        }
    }

    private fun button(label: String, color: Int): TextView {
        return TextView(this).apply {
            text = label
            textSize = 14f
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
            setTextColor(Color.WHITE)
            setPadding(dp(14), dp(13), dp(14), dp(13))
            background = rounded(color, 12)
        }
    }

    private fun text(value: String, size: Float, color: Int, bold: Boolean): TextView =
        TextView(this).apply {
            text = value
            textSize = size
            setTextColor(color)
            if (bold) typeface = Typeface.DEFAULT_BOLD
            textDirection = View.TEXT_DIRECTION_FIRST_STRONG
        }

    private fun rounded(color: Int, radius: Int): GradientDrawable =
        GradientDrawable().apply {
            setColor(color)
            cornerRadius = dp(radius).toFloat()
        }

    private fun marginTop(top: Int): LinearLayout.LayoutParams =
        LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            topMargin = dp(top)
        }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
