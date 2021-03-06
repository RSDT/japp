package nl.rsdt.japp.jotial.net.apis

import nl.rsdt.japp.jotial.data.structures.area348.UserInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author Dingenis Sieger Sinke
 * @version 1.0
 * @since 2-9-2016
 * Description...
 */
interface UserApi {

    @GET("/gebruiker/{key}/info")
    fun getUserInfo(@Path("key") key: String?): Call<UserInfo>

}
