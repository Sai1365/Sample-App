package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.barteksc.pdfviewer.PDFView

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.channels.Channels

class PdfViewer : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)

        val pdfView = findViewById<PDFView>(R.id.pdfView)
        pdfView.fromAsset("BalanceSheet.pdf")
            .enableSwipe(true)
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .load()
    }
}

/***

 * while using webview or downloading i got 404 exception so i simply place that pdf in assets and display.

 ***/

/*    private lateinit var pdfImageView: ImageView
    private val pdfUrl = "https://fssservices.bookxpert.co/GeneratedPDF/Companies/nadc/2024%202025/BalanceSheet.pdf"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)

        pdfImageView = findViewById(R.id.pdfImageView)

        // Download and render the PDF
        downloadAndRenderPdf()
    }

    private fun downloadAndRenderPdf() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Download the PDF
                val file = File(cacheDir, "BalanceSheet.pdf")
                val url = URL(pdfUrl)
                val readableByteChannel = Channels.newChannel(url.openStream())
                val fileOutputStream = FileOutputStream(file)
                fileOutputStream.channel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE)
                fileOutputStream.close()
                readableByteChannel.close()

                // Render the first page as an image using iText
                val bitmap = renderPdfPageToBitmap(file)

                // Display the image on the main thread
                withContext(Dispatchers.Main) {
                    pdfImageView.setImageBitmap(bitmap)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@PdfViewer, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun renderPdfPageToBitmap(pdfFile: File): Bitmap {
        val reader = PdfReader(pdfFile.absolutePath)
        val pageNumber = 1 // Render the first page
        val parser = PdfReaderContentParser(reader)
        val strategy = SimpleTextExtractionStrategy()
        parser.processContent(pageNumber, strategy)

        // Use PdfRenderer or custom rendering logic to convert to Bitmap
        // iText doesn't directly render to UI, so we simulate with a placeholder
        // For actual rendering, consider PdfRenderer or a library like Pdfium
        val bitmap = Bitmap.createBitmap(612, 792, Bitmap.Config.ARGB_8888) // Standard letter size
        // Add custom rendering logic here if needed
        reader.close()
        return bitmap
    }*/
/*


/*    private fun downloadAndDisplayPdf() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Open connection
                val url = URL(pdfUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000
                connection.connect()

                // Check response code
                val responseCode = connection.responseCode
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@PdfViewer,
                            "HTTP Error: $responseCode - ${connection.responseMessage}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    connection.disconnect()
                    return@launch
                }

                // Download the PDF
                val file = File(cacheDir, "BalanceSheet.pdf")
                val inputStream = connection.inputStream
                val fileOutputStream = FileOutputStream(file)
                inputStream.copyTo(fileOutputStream)
                fileOutputStream.close()
                inputStream.close()
                connection.disconnect()

                // Display the PDF on the main thread
                withContext(Dispatchers.Main) {
                    pdfView.fromFile(file)
                        .defaultPage(0)
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .enableDoubletap(true)
                        .load()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@PdfViewer,
                        "Error loading PDF: ${e.javaClass.simpleName} - ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }*/