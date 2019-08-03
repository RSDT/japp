package nl.rsdt.japp.application.fragments

import android.app.Fragment
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_login.*
import nl.rsdt.japp.R
import nl.rsdt.japp.application.AutosAdapter
import nl.rsdt.japp.application.InzittendenAdapter
import nl.rsdt.japp.application.Japp
import nl.rsdt.japp.application.JappPreferences
import nl.rsdt.japp.jotial.data.bodies.AutoUpdateTaakPostBody
import nl.rsdt.japp.jotial.data.structures.area348.AutoInzittendeInfo
import nl.rsdt.japp.jotial.data.structures.area348.DeletedInfo
import nl.rsdt.japp.jotial.net.apis.AutoApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class CarFragment : Fragment(), Callback<HashMap<String, List<AutoInzittendeInfo>>> {
    private var autosRecyclerView: RecyclerView? = null
    private var inzittendeLayout: LinearLayout? = null
    private var autosAdapter: AutosAdapter? = null
    private var inzittendenAdapter: InzittendenAdapter = InzittendenAdapter()
    private var stapUitButton: Button? = null

    override fun onResume() {
        super.onResume()
        refresh()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_tmp_car, container, false)
        // Inflate the layout for this fragment
        autosRecyclerView = v.findViewById(R.id.autos_recycler_view)
        inzittendeLayout = v.findViewById(R.id.inzittenden_linear_view)
        val inzittendenRecyclerView = v.findViewById<RecyclerView>(R.id.inzittenden_recycler_view)
        stapUitButton = v.findViewById(R.id.stap_uit_button)
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        autosRecyclerView?.setHasFixedSize(true)
        inzittendenRecyclerView.setHasFixedSize(true)

        // use a linear layout manager
        val inzittendeLayoutManager = LinearLayoutManager(activity)
        val autoLayoutManager = LinearLayoutManager(activity)

        autosRecyclerView?.layoutManager = autoLayoutManager
        inzittendenRecyclerView.layoutManager = inzittendeLayoutManager

        // specify an adapter (see also next example)
        autosAdapter = AutosAdapter(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                refresh()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                refresh()
            }
        })
        inzittendenAdapter = InzittendenAdapter()
        autosRecyclerView?.adapter = autosAdapter
        inzittendenRecyclerView.adapter = inzittendenAdapter
        stapUitButton?.setOnClickListener {
            val autoApi = Japp.getApi(AutoApi::class.java)
            autoApi.deleteFromCarByName(JappPreferences.accountKey, JappPreferences.accountUsername).enqueue(object : Callback<DeletedInfo> {
                override fun onResponse(call: Call<DeletedInfo>, response: Response<DeletedInfo>) {
                    refresh()
                }

                override fun onFailure(call: Call<DeletedInfo>, t: Throwable) {
                    refresh()
                }
            })
        }
        refresh()
        return v
    }

    private fun findInzittendeInfo(autos: Map<String, List<AutoInzittendeInfo>>): List<AutoInzittendeInfo>? {
        for (auto in autos.values) {
            for (inzittende in auto) {
                if (inzittende.gebruikersNaam == JappPreferences.accountUsername) {
                    return auto
                }
            }
        }
        return null
    }

    fun refresh() {
        val autoApi = Japp.getApi(AutoApi::class.java)
        autoApi.getAllCars(JappPreferences.accountKey).enqueue(this@CarFragment)

    }

    override fun onResponse(call: Call<HashMap<String, List<AutoInzittendeInfo>>>, response: Response<HashMap<String, List<AutoInzittendeInfo>>>) {
        val autos = response.body()?: HashMap()
        val auto = findInzittendeInfo(autos)
        if (auto == null) {
            autosAdapter?.setData(autos)
            autosAdapter?.notifyDataSetChanged()
            autosRecyclerView?.visibility = View.VISIBLE
            inzittendeLayout?.visibility = View.GONE
        } else {

            if (auto[0].autoEigenaar == JappPreferences.accountUsername) {
                stapUitButton?.setText(R.string.remove_from_car)
            } else {
                stapUitButton?.setText(R.string.get_out_of_car)
            }
            val autoApi = Japp.getApi(AutoApi::class.java)
            auto[0].autoEigenaar?.let{autoEigenaar ->
                autoApi.getCarByName(JappPreferences.accountKey, autoEigenaar).enqueue(object : Callback<ArrayList<AutoInzittendeInfo>>{
                    override fun onResponse(call: Call<ArrayList<AutoInzittendeInfo>>, response: Response<ArrayList<AutoInzittendeInfo>>) {
                        val data = response.body()
                        if (response.isSuccessful && data != null){
                            inzittendenAdapter.setData(data)
                            inzittendenAdapter.notifyDataSetChanged()
                            autosRecyclerView?.visibility = View.GONE
                            inzittendeLayout?.visibility = View.VISIBLE
                        }else{
                            inzittendenAdapter.setData(auto)
                            inzittendenAdapter.notifyDataSetChanged()
                            autosRecyclerView?.visibility = View.GONE
                            inzittendeLayout?.visibility = View.VISIBLE
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<AutoInzittendeInfo>>, t: Throwable) {
                        inzittendenAdapter.setData(auto)
                        inzittendenAdapter.notifyDataSetChanged()
                        autosRecyclerView?.visibility = View.GONE
                        inzittendeLayout?.visibility = View.VISIBLE
                    }

                })
            }?:let{
                inzittendenAdapter.setData(auto)
                inzittendenAdapter.notifyDataSetChanged()
                autosRecyclerView?.visibility = View.GONE
                inzittendeLayout?.visibility = View.VISIBLE
            }
        }
    }

    override fun onFailure(call: Call<HashMap<String, List<AutoInzittendeInfo>>>, t: Throwable) {

    }
    companion object {
        val TAG = "CarFragment"
    }
}// Required empty public constructor
