package io.github.liconrepo.mds.sample.pages

import android.os.Bundle
import io.github.liconrepo.mds.sample.databinding.PageProfileBinding

/** The profile page: MdsScreenTemplate filled with a header card, list items and a sign-out action. */
class ProfilePage : PageActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = PageProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSignOut.setOnClickListener { finish() }
    }
}
