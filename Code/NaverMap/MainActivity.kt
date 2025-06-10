package net.test.navermap

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import android.Manifest
import androidx.core.app.ActivityCompat
import net.test.navermap.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    // 지도를 사용할 준비가 된 경우, Map 인스턴스를 제공하는 호출 함수(OnMapReadyCallback) 상속
    private val LOCATION_PERMISSION_REQUEST_CODE = 5000 // 위치 정보 수집 허용 요청 코드

    // 위치 정보 정확성을 높여주는 접근성 허가
    private val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    // binding 과 위치 정보, 네이버 맵 객체 선언
    private lateinit var binding : ActivityMainBinding
    private lateinit var locationSource : FusedLocationSource
    private lateinit var naverMap : NaverMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        // FusedLocationSource 는 지자기, 가속도 센서를 활용해 최적의 위치 정보를 반환하는 구현체
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        // 허가를 받았다면 기기를 통해 맵 띄우고, 받지 않았다면 위치 정보 수집 허용 요청
        if (hasPermission()){
            initMapView()
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    // 맵을 띄우기 위한 준비 함수
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        naverMap.uiSettings.isLocationButtonEnabled = true // 현재 위치를 아이콘으로 표시할 지 여부
        // 움직임 시, 초기화 되는 설정은 방향이 정해지지 않고 현재 위치만 표시하는 None 상태
        if (::naverMap.isInitialized){
            naverMap.locationTrackingMode = LocationTrackingMode.None
        }
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
    }

    // 맵 뷰의 초기 설정 함수
    private fun initMapView() {
        val fm = supportFragmentManager // mapFragment 의 상호작용을 가능하게 하는 역할
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment? // mapFragment 로 불러오고, 객체가 없다면 null 반환
            ?: MapFragment.newInstance().also{ // 객체가 없다면 새로운 mapFragment 생성 후, 레이아웃에 추가
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }
        mapFragment.getMapAsync(this) // 초기화된 mapFragment 를 비동기로 가져오고, 이 객체를 CallbackListener 로 등록
    }

    // 위치 권한이 있을 경우 true 를, 없을 경우 false 를 반환하는 함수
    private fun hasPermission(): Boolean {
        for (permission in PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    // 사용자의 위치 정보 이용 권한 허용 여부에 따른 결과 함수
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (
            locationSource.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
            )
        ){
            if (!locationSource.isActivated){ // 위치 정보가 활성화 되어 있지 않은 경우, 트래킹 모드 초기화
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}