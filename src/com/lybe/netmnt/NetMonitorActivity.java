package com.lybe.netmnt;

import java.util.List;

import com.phone.energymnt.netmnt.R;

import android.app.Activity;
import android.content.Context;
import android.net.TrafficStats;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class NetMonitorActivity extends Activity {
	private TextView tvIsWifi = null;
	private TextView tvScanResult = null;
	private TextView tvWifiConfig = null;
	private TextView tvData = null;
	private WifiManager wifiManager = null;
	private TelephonyManager tm = null;
    private TextView tvExit = null;
    private TextView tvUpdate = null;
    private TextView tvNetMode = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mngInit();
        attrInit();
        update();
    }
    private void mngInit()
    {
        wifiManager = (WifiManager)this.getSystemService(Context.WIFI_SERVICE);
        tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
    }
    private void attrInit()
    {
    	tvIsWifi = (TextView)findViewById(R.id.tvIsWifi);
        tvScanResult = (TextView)findViewById(R.id.tvScanResult);
        tvWifiConfig = (TextView)findViewById(R.id.tvWifiConfig);
        tvData = (TextView)findViewById(R.id.tvData);
        tvUpdate = (TextView)findViewById(R.id.tvUpdate);
        tvExit = (TextView)findViewById(R.id.tvExit);
        tvNetMode = (TextView)findViewById(R.id.tvNetMode);
        tvUpdate.setOnClickListener(new UpdateListener());
        tvExit.setOnClickListener(new TextViewListener());
    }
    public String strListWifiCfg(List<WifiConfiguration> listWifiCfg){
    	String string = "";
    	if(listWifiCfg != null){
    		for(int i = 0;i<listWifiCfg.size();i++){
    			string +=(String)NetMonitorActivity.this.getBaseContext().getResources().getString(R.string.number)
    					+i+"\nBSSID:"+listWifiCfg.get(i).BSSID
    					+"\nSSID: "+listWifiCfg.get(i).SSID
    					+"\nnetwordId: "+listWifiCfg.get(i).networkId+"\n";
    		}
    		return string;
    	}
    	else return "null";
    }
    public String strListScanResult(List<ScanResult> listScanResult){
    	String string = "";
    	if(listScanResult != null){
    		for(int i = 0;i<listScanResult.size();i++){
    			string +=(String)NetMonitorActivity.this.getBaseContext().getResources().getString(R.string.number)
    					+i+"\nBSSID:"+listScanResult.get(i).BSSID
    					+"\nSSID: "+listScanResult.get(i).SSID
    					+"\n";
    		}
    		return string;
    	}
    	else return "null";
    }
    private void update()
    {
    	if(wifiManager.getConnectionInfo() != null && wifiManager.isWifiEnabled())
    	{
    		tvNetMode.setText(
        			(String)NetMonitorActivity.this.getBaseContext().getResources().getString(R.string.modeTitle)
        			+"Wi-Fi");
    	}
    	else
    	{
            switch(tm.getNetworkType())
            {
            case TelephonyManager.NETWORK_TYPE_CDMA:
            	tvNetMode.setText(
            			(String)NetMonitorActivity.this.getBaseContext().getResources().getString(R.string.modeTitle)
            			+"CDMA");
            	break;
            case TelephonyManager.NETWORK_TYPE_EDGE:
            	tvNetMode.setText(
            			(String)NetMonitorActivity.this.getBaseContext().getResources().getString(R.string.modeTitle)
            			+"EDGE");
            	break;
            case TelephonyManager.NETWORK_TYPE_GPRS:
            	tvNetMode.setText(
            			(String)NetMonitorActivity.this.getBaseContext().getResources().getString(R.string.modeTitle)
            			+"GPRS");
            	break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            	tvNetMode.setText(
            			(String)NetMonitorActivity.this.getBaseContext().getResources().getString(R.string.modeTitle)
            			+"UMTS");
            	break;
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
            	tvNetMode.setText(
            			(String)NetMonitorActivity.this.getBaseContext().getResources().getString(R.string.modeTitle)
            			+"UNKNOWN");
            	break;
            	default:
            		break;
            }
    	}
    	if(wifiManager.isWifiEnabled())
		{
			tvIsWifi.setText((String)NetMonitorActivity.this.getBaseContext().getResources().getString(R.string.WifiEnable));
			wifiManager.startScan();
			(Toast.makeText(getApplicationContext(),
					(String)NetMonitorActivity.this.getBaseContext().getResources().getString(R.string.scanning)
					, Toast.LENGTH_SHORT)).show();
    		tvScanResult.setText(strListScanResult(wifiManager.getScanResults()));
    		tvWifiConfig.setText(strListWifiCfg(wifiManager.getConfiguredNetworks()));
		}
        else
        {
        	tvIsWifi.setText((String)NetMonitorActivity.this.getBaseContext().getResources().getString(R.string.WifiDisable));     
        	tvScanResult.setText((String)NetMonitorActivity.this.getBaseContext().getResources().getString(R.string.preWifiEnable));
    		tvWifiConfig.setText((String)NetMonitorActivity.this.getBaseContext().getResources().getString(R.string.preWifiEnable));
        }

        tvData.setText(""
        	+((Long)(TrafficStats.getTotalRxBytes()+TrafficStats.getTotalTxBytes())).toString()
        	+" Bytes"
        	+"\n"
        	+((String)NetMonitorActivity.this.getBaseContext().getResources().getString(R.string.data_ul))
        	+"\n"
        	+((Long)(TrafficStats.getTotalTxBytes())).toString()
        	+" Bytes"
        	+"\n"
        	+((String)NetMonitorActivity.this.getBaseContext().getResources().getString(R.string.data_dl))
        	+"\n"
        	+((Long)(TrafficStats.getTotalRxBytes())).toString()
        	+" Bytes"
        	);
    }
	private class TextViewListener implements OnClickListener
	{
    	@Override
    	public void onClick(View v) 
    	{
    		switch(v.getId())
    		{
    		case R.id.tvExit:
    			NetMonitorActivity.this.finish();break;
    			default:
    				break;
    		}
    	}   	
    }
    private class UpdateListener implements OnClickListener
	{
    	@Override
    	public void onClick(View v) 
    	{
    		update();
    	}   	
    }
}