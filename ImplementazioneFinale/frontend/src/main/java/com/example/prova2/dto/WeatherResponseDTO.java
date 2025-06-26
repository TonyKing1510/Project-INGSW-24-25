package com.example.prova2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponseDTO {
    private double latitude;
    private double longitude;
    private double elevation;
    private String timezone;
    private CurrentWeather currentWeather;
    private HourlyWeather hourlyWeather;
    private DailyWeather daily;

    public static class CurrentWeather {
        private String time;
        private double temperature;
        private double windspeed;
        private int winddirection;
        private int weathercode;

        public CurrentWeather(){}

        public double getTemperature() {
            return temperature;
        }

        public double getWindspeed() {
            return windspeed;
        }

        public int getWeathercode() {
            return weathercode;
        }

        public int getWinddirection() {
            return winddirection;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public void setTemperature(double temperature) {
            this.temperature = temperature;
        }

        public void setWeathercode(int weathercode) {
            this.weathercode = weathercode;
        }

        public void setWinddirection(int winddirection) {
            this.winddirection = winddirection;
        }

        public void setWindspeed(double windspeed) {
            this.windspeed = windspeed;
        }
    }

    public static class HourlyWeather {
        private List<String> time;
        private List<Double> temperature_2m;
        private List<Double> precipitation;

        public HourlyWeather(){}

        public List<String> getTime() {
            return time;
        }

        public List<Double> getPrecipitation() {
            return precipitation;
        }

        public List<Double> getTemperature_2m() {
            return temperature_2m;
        }

        public void setTime(List<String> time) {
            this.time = time;
        }

        public void setPrecipitation(List<Double> precipitation) {
            this.precipitation = precipitation;
        }

        public void setTemperature_2m(List<Double> temperature_2m) {
            this.temperature_2m = temperature_2m;
        }
    }

    public static class DailyWeather {
        private List<String> time;
        private List<Integer> temperature_2m_max;
        private List<Double> temperature_2m_min;
        private List<Integer> precipitation_sum;

        public DailyWeather(){}

        public List<Integer> getPrecipitation_sum() {
            return precipitation_sum;
        }

        public List<Integer> getTemperature_2m_max() {
            return temperature_2m_max;
        }

        public List<Double> getTemperature_2m_min() {
            return temperature_2m_min;
        }

        public List<String> getTime() {
            return time;
        }

        public void setPrecipitation_sum(List<Integer> precipitation_sum) {
            this.precipitation_sum = precipitation_sum;
        }

        public void setTemperature_2m_max(List<Integer> temperature_2m_max) {
            this.temperature_2m_max = temperature_2m_max;
        }

        public void setTemperature_2m_min(List<Double> temperature_2m_min) {
            this.temperature_2m_min = temperature_2m_min;
        }

        public void setTime(List<String> time) {
            this.time = time;
        }
    }

    public CurrentWeather getCurrentWeather() {
        return currentWeather;
    }

    public double getElevation() {
        return elevation;
    }

    public DailyWeather getDaily() {
        return daily;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public HourlyWeather getHourlyWeather() {
        return hourlyWeather;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setCurrentWeather(CurrentWeather currentWeather) {
        this.currentWeather = currentWeather;
    }

    public void setDaily(DailyWeather daily) {
        this.daily = daily;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public void setHourlyWeather(HourlyWeather hourlyWeather) {
        this.hourlyWeather = hourlyWeather;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}

