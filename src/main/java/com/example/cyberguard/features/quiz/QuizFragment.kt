package com.example.cyberguard.features.quiz

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.cyberguard.R
import com.example.cyberguard.core.ToastUtils
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class QuizFragment : Fragment() {

    private val beginnerQuestions = listOf(
        Question("What is the most secure way to create a password?",
            listOf("Using personal information like your birthday", "A random combination of letters, numbers, and symbols", "A common word with a number at the end", "The same password for all accounts"),
            1, "A strong password uses a random mix of letters, numbers, and symbols."),
        Question("What should you do if you receive a suspicious email?",
            listOf("Click any links to see where they go", "Reply with your personal information", "Delete it or report it as spam", "Forward it to your friends"),
            2, "Suspicious emails should be deleted or reported as spam. Never click links or share personal info."),
        Question("Which of these is a sign of a phishing attempt?",
            listOf("An email from your bank with your name", "An email with poor grammar and urgent requests", "A message from a known contact", "A newsletter you subscribed to"),
            1, "Phishing emails often have poor grammar and create a sense of urgency."),
        Question("What is malware?",
            listOf("A type of hardware", "A malicious software", "A secure website", "A password manager"),
            1, "Malware is software designed to harm or exploit any programmable device or network."),
        Question("Which is a safe way to browse the internet?",
            listOf("Clicking on pop-up ads", "Using strong, unique passwords", "Downloading files from unknown sources", "Sharing your passwords"),
            1, "Using strong, unique passwords helps keep your accounts safe."),
        Question("What should you do before clicking a link in an email?",
            listOf("Click it immediately", "Hover to preview the URL", "Forward to friends", "Reply to the sender"),
            1, "Hovering over a link lets you see the real URL before clicking."),
        Question("Which device is most at risk from malware?",
            listOf("A device with no antivirus", "A device with updated security", "A device never connected to the internet", "A device with strong passwords"),
            0, "Devices without antivirus are more vulnerable to malware."),
        Question("What is a strong PIN for your phone?",
            listOf("1234", "0000", "A random 6-digit number", "Your birth year"),
            2, "A random 6-digit number is much harder to guess."),
        Question("What is the best way to keep your software safe?",
            listOf("Never update it", "Update regularly", "Ignore security warnings", "Use the same password everywhere"),
            1, "Regular updates patch security vulnerabilities."),
        Question("What is a secure way to use public Wi-Fi?",
            listOf("Access bank accounts", "Use a VPN", "Share personal info", "Turn off your firewall"),
            1, "A VPN encrypts your data on public Wi-Fi."),
    )

    private val intermediateQuestions = listOf(
        Question("What is two-factor authentication (2FA)?",
            listOf("Two passwords", "A second login step", "A backup email", "An antivirus scan"),
            1, "2FA adds a second step (like a code sent to your phone) to make accounts more secure."),
        Question("What is ransomware?",
            listOf("Free software", "A tool to speed up your PC", "Software that hijacks files", "A browser extension"),
            2, "Ransomware is malicious software that locks your files and demands payment to unlock them."),
        Question("What is a firewall?",
            listOf("A physical wall", "A security system for networks", "A type of malware", "A password manager"),
            1, "A firewall helps block unauthorized access to your network."),
        Question("Which is a secure way to store passwords?",
            listOf("Write them on paper", "Use a password manager", "Email them to yourself", "Use the same password everywhere"),
            1, "Password managers securely store and encrypt your passwords."),
        Question("What is phishing?",
            listOf("A type of malware", "A fake website or email to steal info", "A secure login method", "A backup process"),
            1, "Phishing tricks you into giving up personal information."),
        Question("What is a VPN used for?",
            listOf("Speeding up your internet", "Encrypting your internet connection", "Making phone calls", "Sending emails"),
            1, "A VPN encrypts your internet connection for privacy and security."),
        Question("What is a botnet?",
            listOf("A network of infected computers", "A type of password", "A secure website", "A backup system"),
            0, "A botnet is a network of computers infected and controlled by malware."),
        Question("What is social engineering?",
            listOf("Building social networks", "Manipulating people to reveal info", "A type of encryption", "A firewall setting"),
            1, "Social engineering manipulates people into giving up confidential info."),
        Question("What is the best way to recognize a secure website?",
            listOf("It has lots of ads", "It uses HTTP", "It uses HTTPS and a padlock icon", "It asks for your password immediately"),
            2, "HTTPS and a padlock icon indicate a secure website."),
        Question("What is a common sign of a compromised account?",
            listOf("You can log in normally", "You receive password reset emails you didn't request", "You get a thank you email", "You see no changes"),
            1, "Unexpected password reset emails can mean your account is compromised."),
    )

    private val expertQuestions = listOf(
        Question("What is a 'zero-day' vulnerability?",
            listOf("A bug found on the first day", "A flaw exploited before a fix exists", "A password that never expires", "A secure network"),
            1, "A zero-day is a security flaw that is exploited before the developer has released a fix."),
        Question("What does a DDoS attack do?",
            listOf("Steals your data", "Overwhelms a server with traffic", "Encrypts your files", "Deletes your account"),
            1, "A DDoS attack floods a server with traffic, making it unavailable to users."),
        Question("What is SQL injection?",
            listOf("A type of vaccine", "A database attack technique", "A password manager", "A firewall setting"),
            1, "SQL injection is a technique to attack databases by injecting malicious code."),
        Question("What is a digital certificate?",
            listOf("A paper document", "A file that verifies website identity", "A type of malware", "A password"),
            1, "A digital certificate verifies the identity of a website or user."),
        Question("What is multi-factor authentication (MFA)?",
            listOf("Using multiple passwords", "Using two or more verification methods", "A type of malware", "A backup process"),
            1, "MFA uses two or more verification methods for better security."),
        Question("What is spear phishing?",
            listOf("A phishing attack targeting a specific person", "A type of firewall", "A password manager", "A backup process"),
            0, "Spear phishing targets specific individuals with personalized messages."),
        Question("What is a rootkit?",
            listOf("A gardening tool", "Malware that hides itself on a system", "A password manager", "A type of encryption"),
            1, "A rootkit is malware designed to hide its presence on a system."),
        Question("What is the main purpose of encryption?",
            listOf("To speed up your computer", "To protect data by making it unreadable without a key", "To delete files", "To create backups"),
            1, "Encryption protects data by making it unreadable without the correct key."),
        Question("What is a man-in-the-middle attack?",
            listOf("An attack where someone intercepts communication", "A type of password", "A backup process", "A firewall setting"),
            0, "A man-in-the-middle attack is when someone secretly intercepts and possibly alters communication between two parties."),
        Question("What is the best way to protect against phishing?",
            listOf("Click all links", "Be cautious with emails and verify senders", "Share your password", "Ignore security updates"),
            1, "Always verify email senders and be cautious with links to avoid phishing."),
    )

    private lateinit var questionText: TextView
    private lateinit var optionsContainer: LinearLayout
    private lateinit var nextButton: Button
    private lateinit var restartButton: Button
    private lateinit var levelSelectionContainer: LinearLayout
    private lateinit var beginnerButton: Button
    private lateinit var intermediateButton: Button
    private lateinit var expertButton: Button
    private lateinit var progressText: TextView
    private lateinit var explanationBox: LinearLayout
    private lateinit var explanationText: TextView
    private lateinit var quizContentContainer: LinearLayout

    private var questions: List<Question> = emptyList()
    private var currentQuestionIndex = 0
    private var score = 0
    private var answered = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_quiz, container, false)
        questionText = view.findViewById(R.id.questionText)
        optionsContainer = view.findViewById(R.id.optionsContainer)
        nextButton = view.findViewById(R.id.nextButton)
        restartButton = view.findViewById(R.id.restartButton)
        levelSelectionContainer = view.findViewById(R.id.levelSelectionContainer)
        beginnerButton = view.findViewById(R.id.beginnerButton)
        intermediateButton = view.findViewById(R.id.intermediateButton)
        expertButton = view.findViewById(R.id.expertButton)
        progressText = view.findViewById(R.id.progressText)
        explanationBox = view.findViewById(R.id.explanationBox)
        explanationText = view.findViewById(R.id.explanationText)
        quizContentContainer = view.findViewById(R.id.quizContentContainer)

        beginnerButton.setOnClickListener { startQuiz(beginnerQuestions) }
        intermediateButton.setOnClickListener { startQuiz(intermediateQuestions) }
        expertButton.setOnClickListener { startQuiz(expertQuestions) }
        nextButton.setOnClickListener { onNext() }
        restartButton.setOnClickListener { restartQuiz() }

        showLevelSelection()
        return view
    }

    private fun showLevelSelection() {
        levelSelectionContainer.visibility = View.VISIBLE
        quizContentContainer.visibility = View.GONE
        questionText.visibility = View.GONE
        optionsContainer.visibility = View.GONE
        nextButton.visibility = View.GONE
        restartButton.visibility = View.GONE
        explanationBox.visibility = View.GONE
    }

    private fun startQuiz(selectedQuestions: List<Question>) {
        questions = selectedQuestions
        currentQuestionIndex = 0
        score = 0
        answered = false
        levelSelectionContainer.visibility = View.GONE
        quizContentContainer.visibility = View.VISIBLE
        questionText.visibility = View.VISIBLE
        optionsContainer.visibility = View.VISIBLE
        nextButton.visibility = View.VISIBLE
        restartButton.visibility = View.GONE
        loadQuestion()
    }

    private fun loadQuestion() {
        val question = questions[currentQuestionIndex]
        questionText.text = question.text
        optionsContainer.removeAllViews()
        explanationBox.visibility = View.GONE
        progressText.text = "Question ${currentQuestionIndex + 1} of ${questions.size}"
        question.options.forEachIndexed { index, option ->
            val button = MaterialButton(requireContext(), null, com.google.android.material.R.attr.materialButtonOutlinedStyle)
            button.text = option
            button.isAllCaps = false
            button.setTextColor(resources.getColor(R.color.text_primary, null))
            button.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 8, 0, 8)
            }
            button.setOnClickListener {
                if (!answered) checkAnswer(index, button)
            }
            optionsContainer.addView(button)
        }
        nextButton.isEnabled = false
        nextButton.text = if (currentQuestionIndex < questions.size - 1) "Next" else "Finish"
        answered = false
    }

    private fun checkAnswer(selectedIndex: Int, selectedButton: Button) {
        val correctIndex = questions[currentQuestionIndex].correctAnswerIndex
        answered = true
        
        val correctButton = optionsContainer.getChildAt(correctIndex) as MaterialButton
        
        if (selectedIndex == correctIndex) {
            selectedButton.setBackgroundColor(resources.getColor(R.color.accent_success, null))
            selectedButton.setTextColor(resources.getColor(R.color.white, null))
            ToastUtils.showToast(requireContext(), "✅ Correct!", "quiz_correct")
            explanationText.text = questions[currentQuestionIndex].explanation
        } else {
            selectedButton.setBackgroundColor(resources.getColor(R.color.accent_error, null))
            selectedButton.setTextColor(resources.getColor(R.color.white, null))
            correctButton.setBackgroundColor(resources.getColor(R.color.accent_success, null))
            correctButton.setTextColor(resources.getColor(R.color.white, null))
            ToastUtils.showToast(requireContext(), "❌ Incorrect!", "quiz_incorrect")
            val correctAnswer = questions[currentQuestionIndex].options[correctIndex]
            explanationText.text = questions[currentQuestionIndex].explanation + "\n\n✅ Correct answer: " + correctAnswer
        }
        
        explanationBox.visibility = View.VISIBLE
        for (i in 0 until optionsContainer.childCount) {
            optionsContainer.getChildAt(i).isEnabled = false
        }
        nextButton.isEnabled = true
        if (selectedIndex == correctIndex) score++
    }

    private fun onNext() {
        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
            loadQuestion()
        } else {
            showSummary()
        }
    }

    private fun showSummary() {
        questionText.text = "Quiz Completed!"
        optionsContainer.removeAllViews()
        explanationBox.visibility = View.GONE
        val summary = TextView(context)
        summary.text = "Your Score: $score / ${questions.size}"
        summary.textSize = 18f
        summary.setTextColor(resources.getColor(R.color.text_primary, null))
        summary.setPadding(0, 32, 0, 16)
        summary.gravity = Gravity.CENTER
        optionsContainer.addView(summary)
        nextButton.visibility = View.GONE
        restartButton.visibility = View.VISIBLE
    }

    private fun restartQuiz() {
        showLevelSelection()
    }

    data class Question(
        val text: String,
        val options: List<String>,
        val correctAnswerIndex: Int,
        val explanation: String
    )
}
