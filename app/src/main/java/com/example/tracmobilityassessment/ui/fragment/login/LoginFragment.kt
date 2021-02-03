package com.example.tracmobilityassessment.ui.fragment.login

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.auth0.android.Auth0
import com.auth0.android.lock.AuthenticationCallback
import com.auth0.android.lock.Lock
import com.auth0.android.lock.utils.LockException
import com.auth0.android.result.Credentials
import com.example.tracmobilityassessment.ui.activity.MainActivity
import com.example.tracmobilityassessment.R
import com.example.tracmobilityassessment.logic.enums.AuthenticationType
import com.example.tracmobilityassessment.logic.enums.SnackBarLength
import com.example.tracmobilityassessment.logic.managers.SnackbarManager
import com.example.tracmobilityassessment.logic.managers.UserManager
import com.example.tracmobilityassessment.ui.fragment.map.GoogleMapFragment
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*


class LoginFragment : Fragment(), View.OnClickListener {

    private lateinit var mView: View
    private lateinit var callbackManager: CallbackManager
    private val rcSignIn = 1
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        mView = inflater.inflate(R.layout.fragment_login, container, false)
        setOnClickListeners()
        firebaseAuth = FirebaseAuth.getInstance()
        configureGoogleSignIn()
        configureFacebookLogin()
        return mView
    }

    private fun setOnClickListeners() {
        mView.btn_signup.setOnClickListener(this)
        mView.btn_login_google.setOnClickListener(this)
        mView.btn_login_facebook.setOnClickListener(this)
        mView.btn_login.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {

            R.id.btn_signup -> {
                signup(activity as MainActivity, requireContext())
            }
            R.id.btn_login_google -> {
                val signInIntent: Intent = googleSignInClient.signInIntent
                startActivityForResult(signInIntent, rcSignIn)
            }
            R.id.btn_login_facebook -> {
                LoginManager.getInstance()
                    .logInWithReadPermissions(this, listOf("email", "public_profile"))
            }
            R.id.btn_login -> {
                if (validateFields()) {
                    val fullName =
                        "${et_login_firstname.text.toString()} ${et_login_lastname.text.toString()}"
                    UserManager.login(
                        fullName,
                        et_login_email.text.toString(),
                        requireContext(),
                        null
                    )
                    (requireActivity() as MainActivity).openFragment(GoogleMapFragment())
                }
            }
        }
    }

    private fun validateFields(): Boolean {
        var isValid = true
        til_login_email.error = ""
        til_login_firstname.error = ""
        til_login_lastname.error = ""

        if (et_login_email.text.toString().isEmpty()) {
            til_login_email.error = requireContext().getString(R.string.login_email_errortext)
            isValid = false
        }
        if (et_login_firstname.text.toString().isEmpty()) {
            til_login_firstname.error =
                requireContext().getString(R.string.login_firstname_errortext)
            isValid = false
        }
        if (et_login_lastname.text.toString().isEmpty()) {
            til_login_lastname.error = requireContext().getString(R.string.login_lastname_errortext)
            isValid = false
        }
        return isValid
    }

    private fun configureGoogleSignIn() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context?.getString(R.string.google_webclient_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(
            requireActivity(),
            googleSignInOptions
        )
    }

    private fun configureFacebookLogin() {
        FacebookSdk.sdkInitialize(requireContext())
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    handleFacebookAccessToken(loginResult!!.accessToken)
                }

                override fun onCancel() {
                    if (!doesUserHaveFacebookAppInstalled(context))
                        SnackbarManager(requireContext(), requireView())
                            .showSnackBar(
                                getString(
                                    R.string.login_facebook_login_not_installed
                                ), SnackBarLength.LONG
                            )
                    val accessToken = AccessToken.getCurrentAccessToken()
                    accessToken?.let { handleFacebookAccessToken(it) }
                }

                override fun onError(exception: FacebookException) {
                }
            })
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            firebaseAuthWithGoogle(account)
        } catch (e: ApiException) {
            SnackbarManager(requireContext(), requireView())
                .showSnackBar(
                    getString(R.string.login_google_Sign_in_is_failed),
                    SnackBarLength.LONG
                )
        }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    loginWithFirebaseAccount(user!!, AuthenticationType.Facebook)
                } else {
                    SnackbarManager(requireContext(), requireView())
                        .showSnackBar(
                            getString(R.string.login_facebook_login_is_failed),
                            SnackBarLength.LONG
                        )
                }
            }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    loginWithFirebaseAccount(user!!, AuthenticationType.Google)
                } else {
                    SnackbarManager(requireContext(), requireView())
                        .showSnackBar(
                            getString(R.string.login_google_Sign_in_is_failed),
                            SnackBarLength.LONG
                        )
                }
            }
    }

    private fun loginWithFirebaseAccount(
        firebaseUser: FirebaseUser,
        authenticationMode: Enum<AuthenticationType>
    ) {
        UserManager.login(
            firebaseUser.displayName!!,
            firebaseUser.email!!,
            requireContext(),
            firebaseUser.photoUrl.toString()
        )
        (activity as MainActivity).openFragment(GoogleMapFragment())
    }

    private fun doesUserHaveFacebookAppInstalled(context: Context?): Boolean {
        return try {
            context?.packageManager?.getApplicationInfo(
                context.getString(R.string.facebook_app_package_name),
                0
            )
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == rcSignIn) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun signup(activity: MainActivity, context: Context) {
        val auth0: Auth0 = Auth0(activity)

        val lock = Lock.newBuilder(auth0, object : AuthenticationCallback() {
            override fun onError(error: LockException) {
                TODO("Not yet implemented")
            }

            override fun onAuthentication(credentials: Credentials) {
                TODO("Not yet implemented")
            }

            override fun onCanceled() {
                TODO("Not yet implemented")
            }

        })
            .withScheme("demo")
            .withAudience(
                String.format(
                    "https://%s/userinfo",
                    requireActivity().getString(R.string.com_auth0_domain)
                )
            )
            .build(context)
        startActivity(lock.newIntent(context))
    }
}