package com.example.idcardscanner

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var btnScan: Button
    private lateinit var btnExportPDF: Button
    private lateinit var imgPreview: ImageView
    private lateinit var tvResult: TextView
    private var scannedText = ""
    private val PICK_IMAGE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnScan = findViewById(R.id.btnScan)
        btnExportPDF = findViewById(R.id.btnExportPDF)
        imgPreview = findViewById(R.id.imgPreview)
        tvResult = findViewById(R.id.tvResult)

        btnScan.setOnClickListener {
            ImagePicker.with(this)
                .cameraOnly()
                .crop()
                .compress(1024)
                .start(PICK_IMAGE)
        }

        btnExportPDF.setOnClickListener {
            exportToPDF()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val uri: Uri = data?.data!!
            imgPreview.setImageURI(uri)
            processImage(uri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        }
    }

    private fun processImage(uri: Uri) {
        val image = InputImage.fromFilePath(this, uri)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                scannedText = visionText.text
                tvResult.text = scannedText
                btnExportPDF.isEnabled = true
                Toast.makeText(this, "Text Extracted!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun exportToPDF() {
        try {
            val pdfFile = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "IDCard_${System.currentTimeMillis()}.pdf")
            val document = Document()
            PdfWriter.getInstance(document, FileOutputStream(pdfFile))
            document.open()
            document.add(Paragraph("Scanned ID Card Data"))
            document.add(Paragraph("\n"))
            document.add(Paragraph(scannedText))
            document.close()

            Toast.makeText(this, "PDF Saved: ${pdfFile.path}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, "PDF Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}