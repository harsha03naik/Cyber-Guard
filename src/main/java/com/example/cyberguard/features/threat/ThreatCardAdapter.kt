package com.example.cyberguard.features.threat

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cyberguard.databinding.ItemThreatCardBinding
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.graphics.Typeface
import androidx.core.content.ContextCompat

data class ThreatCard(val title: String, val description: String, val iconRes: Int)

class ThreatCardAdapter(
    private val context: Context,
    private val threatList: List<ThreatCard>
) : RecyclerView.Adapter<ThreatCardAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemThreatCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemThreatCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val threat = threatList[position]
        with(holder.binding) {
            threatTitle.text = threat.title
            threatDescription.text = threat.description
            threatIcon.setImageResource(threat.iconRes)
            cardThreat.setOnClickListener {
                showDetailsDialog(context, threat)
            }
        }
    }

    override fun getItemCount(): Int = threatList.size

    private fun showDetailsDialog(context: Context, threat: ThreatCard) {
        val detailedMessage = when (threat.title) {
            "Phishing Attacks" -> """
                What is it?
                A fraudulent attempt to obtain sensitive information (like passwords or credit card details) by disguising as a trustworthy entity in an electronic communication.

                How to spot it?
                • Look for generic greetings (e.g., "Dear Customer").
                • Check for poor grammar and spelling mistakes.
                • Hover over links to see the actual URL before clicking.

                How to prevent it?
                • Never click on suspicious links or attachments.
                • Use a good spam filter and enable 2FA.
            """.trimIndent()
            "Malware" -> """
                What is it?
                Malicious software—including viruses, worms, and spyware—designed to disrupt, damage, or gain unauthorized access to a computer system.

                How to spot it?
                • Your computer slows down, freezes, or crashes often.
                • You see unexpected pop-up ads.

                How to prevent it?
                • Use trusted antivirus software and keep it updated.
                • Avoid downloading from untrustworthy sources.
            """.trimIndent()
            "Social Engineering" -> """
                What is it?
                The art of manipulating people into performing actions or divulging confidential information. It relies on human interaction and error.

                How to spot it?
                • Someone creating a sense of urgency or fear.
                • An offer that seems too good to be true.

                How to prevent it?
                • Be suspicious of unsolicited requests for personal information.
                • Verify the person's identity through an official channel.
            """.trimIndent()
            "Ransomware" -> """
                What is it?
                A type of malware that encrypts your files, making them inaccessible, and demands a ransom payment to restore access.

                How to spot it?
                • You can no longer open your files.
                • A message appears on your screen demanding payment.

                How to prevent it?
                • Regularly back up your important files to an external drive or cloud service.
                • Avoid opening suspicious email attachments.
            """.trimIndent()
            else -> "No additional details available."
        }

        AlertDialog.Builder(context)
            .setTitle(threat.title)
            .setMessage(detailedMessage)
            .setIcon(threat.iconRes)
            .setPositiveButton("OK", null)
            .show()
    }
}
