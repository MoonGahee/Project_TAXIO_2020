package com.example.project_taxio_2020;

import android.content.ContentValues;
import android.os.StrictMode;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

// 날씨 뭐시긴지 모르겠습니다.

public class ForeCastManager extends Thread {
    String lon,lat;
    ArrayList<ContentValues> mWeather;
    generalMainActivity mContext;
    public ArrayList<ContentValues> getmWeather()
    {
        return mWeather;
    }

    public ForeCastManager(String lon, String lat, generalMainActivity mContext)
    {
        this.lon = lon ; this.lat = lat;
        this.mContext = mContext;
    }

    public ArrayList<ContentValues> GetOpenWeather(String lon,String lat)
    {

        ArrayList<ContentValues> mTotalValue = new ArrayList<ContentValues>();
        String key = "53c109932ee55a016927343760f79a58";
        try{
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?id=524901" +
                    "&appid=" + key +
                    "&lat="+lat+
                    "&lon="+lon+
                    "&mode=xml" +
                    "&units=metric"+
                    "&cnt=" + 6 );


            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            // 위에서 생성된 URL을 통하여 서버에 요청하면 결과가 XML Resource로 전달됨
            XmlPullParser parser = factory.newPullParser();
            // XML Resource를 파싱할 parser를 factory로 생성
            parser.setInput(url.openStream(), null);
            // 파서를 통하여 각 요소들의 이벤트성 처리를 반복수행
            int parserEvent = parser.getEventType();
            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                if(parserEvent == XmlPullParser.START_TAG  && parser.getName().equals("coord")){
                    //시작태그의 이름을 알아냄
                    int checkStartTag = parserEvent;
                    ContentValues mContent = new ContentValues();

                    for( ; ; ) {
                       if (checkStartTag == XmlPullParser.START_TAG  && parser.getName().equals("direction")) {
                            mContent.put("wind_Direction", parser.getAttributeValue(null, "name"));
                            mContent.put("wind_SortNumber", parser.getAttributeValue(null, "deg"));
                            mContent.put("wind_SortCode", parser.getAttributeValue(null, "code"));
                        } else if (checkStartTag == XmlPullParser.START_TAG  && parser.getName().equals("speed")) {
                            mContent.put("wind_Speed", parser.getAttributeValue(null, "value"));
                            mContent.put("wind_Name", parser.getAttributeValue(null, "name"));
                        } else if (checkStartTag == XmlPullParser.START_TAG  && parser.getName().equals("temperature")) {
                            mContent.put("temp_Min", parser.getAttributeValue(null, "min"));
                            mContent.put("temp_Max", parser.getAttributeValue(null, "max"));
                            mContent.put("temp_value", parser.getAttributeValue(null, "value"));
                        }else if (checkStartTag == XmlPullParser.START_TAG  && parser.getName().equals("humidity")) {
                            mContent.put("humidity", parser.getAttributeValue(null, "value"));
                            mContent.put("humidity_unit", parser.getAttributeValue(null, "unit"));
                        }else if (checkStartTag == XmlPullParser.START_TAG && parser.getName().equals("feels_like")){
                            mContent.put("feel_like_value", parser.getAttributeValue(null, "value"));
                            mContent.put("feel_like_unit", parser.getAttributeValue(null, "unit"));
                        }else if (checkStartTag == XmlPullParser.START_TAG  && parser.getName().equals("weather")) {
                            mContent.put("weather_Number", parser.getAttributeValue(null, "number"));
                            mContent.put("weather_Name", parser.getAttributeValue(null, "value"));
                            mContent.put("weather_Much", parser.getAttributeValue(null, "icon"));
                            mTotalValue.add(mContent);
                            break;
                        }
                        checkStartTag = parser.next();
                    }

                }
                parserEvent = parser.next();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mTotalValue;
    }

    @Override
    public void run() {
        super.run();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        mWeather = GetOpenWeather(lon,lat);
        mContext.handler.sendEmptyMessage(mContext.THREAD_HANDLER_SUCCESS_INFO);
    }
}
