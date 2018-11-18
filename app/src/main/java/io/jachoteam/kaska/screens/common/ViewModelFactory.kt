package io.jachoteam.kaska.screens.common

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import io.jachoteam.kaska.screens.InstagramApp
import io.jachoteam.kaska.screens.addfriends.AddFriendsViewModel
import io.jachoteam.kaska.screens.comments.CommentsViewModel
import io.jachoteam.kaska.screens.editprofile.EditProfileViewModel
import io.jachoteam.kaska.screens.home.HomeViewModel
import io.jachoteam.kaska.screens.login.LoginViewModel
import io.jachoteam.kaska.screens.notifications.NotificationsViewModel
import io.jachoteam.kaska.screens.profile.ProfileViewModel
import io.jachoteam.kaska.screens.profilesettings.ProfileSettingsViewModel
import io.jachoteam.kaska.screens.register.RegisterViewModel
import io.jachoteam.kaska.screens.search.SearchViewModel
import io.jachoteam.kaska.screens.share.ShareViewModel
import com.google.android.gms.tasks.OnFailureListener
import io.jachoteam.kaska.data.FeedPostsRepository
import io.jachoteam.kaska.screens.postDetails.PostDetailsViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val app: InstagramApp,
                       private val commonViewModel: CommonViewModel,
                       private val onFailureListener: OnFailureListener) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val usersRepo = app.usersRepo
        val feedPostsRepo = app.feedPostsRepo
        val authManager = app.authManager
        val notificationsRepo = app.notificationsRepo
        val searchRepo = app.searchRepo

        if (modelClass.isAssignableFrom(AddFriendsViewModel::class.java)) {
            return AddFriendsViewModel(onFailureListener, usersRepo, feedPostsRepo) as T
        } else if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
            return EditProfileViewModel(onFailureListener, usersRepo) as T
        } else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(onFailureListener, feedPostsRepo) as T
        } else if (modelClass.isAssignableFrom(ProfileSettingsViewModel::class.java)) {
            return ProfileSettingsViewModel(authManager, onFailureListener) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(authManager, app, commonViewModel, onFailureListener) as T
        } else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(usersRepo, onFailureListener) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(commonViewModel, app, onFailureListener, usersRepo) as T
        } else if (modelClass.isAssignableFrom(ShareViewModel::class.java)) {
            return ShareViewModel(feedPostsRepo, usersRepo, onFailureListener) as T
        } else if (modelClass.isAssignableFrom(CommentsViewModel::class.java)) {
            return CommentsViewModel(feedPostsRepo, usersRepo, onFailureListener) as T
        } else if (modelClass.isAssignableFrom(NotificationsViewModel::class.java)) {
            return NotificationsViewModel(notificationsRepo, onFailureListener) as T
        } else if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(searchRepo, onFailureListener) as T
        } else if (modelClass.isAssignableFrom(PostDetailsViewModel::class.java)) {
            return PostDetailsViewModel(onFailureListener, feedPostsRepo) as T
        } else {
            error("Unknown view model class $modelClass")
        }
    }
}