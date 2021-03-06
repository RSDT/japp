package nl.rsdt.japp.jotial.maps.management.controllers

import android.os.Bundle
import nl.rsdt.japp.jotial.maps.wrapper.ICircle
import nl.rsdt.japp.jotial.maps.wrapper.IJotiMap
import java.util.ArrayList

/**
 * @author Dingenis Sieger Sinke
 * @version 1.0
 * @since 31-7-2016
 * Description...
 */

/**
 * @author Dingenis Sieger Sinke
 * @version 1.0
 * @since 31-7-2016
 * Description...
 */
class BravoVosController(jotiMap: IJotiMap) : VosController(jotiMap) {


    override val team: String
        get() = "b"

    override val id: String
        get() = CONTROLLER_ID

    override val storageId: String
        get() = STORAGE_ID

    override val bundleId: String
        get() = BUNDLE_ID

    companion object {

        val CONTROLLER_ID = "BravoVosController"

        val STORAGE_ID = "STORAGE_VOS_B"

        val BUNDLE_ID = "VOS_B"

        val REQUEST_ID = "REQUEST_VOS_B"
    }

}

