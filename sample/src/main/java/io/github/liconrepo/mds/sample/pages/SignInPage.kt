package io.github.liconrepo.mds.sample.pages

import android.os.Bundle
import android.util.Patterns
import io.github.liconrepo.mds.perceptual.MdsTone
import io.github.liconrepo.mds.sample.R
import io.github.liconrepo.mds.sample.databinding.PageSignInBinding

/** The sign-in page: MdsScreenTemplate filled with an alert, two text fields and two buttons. */
class SignInPage : PageActivity() {

    private lateinit var binding: PageSignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PageSignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignIn.setOnClickListener { submit() }
        binding.btnCreateAccount.setOnClickListener { finish() }
    }

    private fun submit() {
        val email = binding.fieldEmail.text?.toString().orEmpty().trim()
        val password = binding.fieldPassword.text?.toString().orEmpty()
        binding.fieldEmail.errorText = when {
            email.isEmpty() -> getString(R.string.error_email_empty)
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> getString(R.string.error_email_invalid)
            else -> null
        }
        binding.fieldPassword.errorText = if (password.length < 8) getString(R.string.error_password_short) else null

        if (binding.fieldEmail.errorText == null && binding.fieldPassword.errorText == null) {
            binding.alert.tone = MdsTone.Success
            binding.alert.title = getString(R.string.signed_in_title)
            binding.alert.message = getString(R.string.signed_in_message)
        }
    }
}
