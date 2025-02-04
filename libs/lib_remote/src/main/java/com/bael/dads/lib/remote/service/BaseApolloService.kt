package com.bael.dads.lib.remote.service

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Operation.Data
import com.apollographql.apollo.api.Operation.Variables
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.coroutines.await
import com.bael.dads.domain.common.exception.NoNetworkException
import com.bael.dads.lib.remote.network.Network
import java.io.IOException
import javax.inject.Inject

/**
 * Created by ErickSumargo on 01/01/21.
 */

abstract class BaseApolloService(private val client: ApolloClient) {
    @Inject
    internal lateinit var network: Network

    protected suspend fun <D : Data> query(script: Query<D, D, Variables>): Result<D> {
        return runCatching {
            if (!network.isConnected) throw NoNetworkException()

            val response = client.query(script).await()
            if (response.hasErrors()) {
                throw IOException(response.errors!![0].message)
            }
            response.data!!
        }
    }
}
