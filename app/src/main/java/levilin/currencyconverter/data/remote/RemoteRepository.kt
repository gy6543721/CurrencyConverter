package levilin.currencyconverter.data.remote

import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class RemoteRepository @Inject constructor(remoteDataSource: RemoteDataSource) {
    val remoteDataSource = remoteDataSource
}