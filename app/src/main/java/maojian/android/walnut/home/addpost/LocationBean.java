package maojian.android.walnut.home.addpost;

import java.io.Serializable;
import java.util.List;

/**
 * @author hezuzhi
 * @Description: ()
 * @date 2017/3/17  9:57.
 * @version: 1.0
 */
public class LocationBean implements Serializable {
    private List<results> results;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<LocationBean.results> getResults() {
        return results;
    }

    public List<LocationBean.results> getResultsList() {
        LocationBean.results results1 = new results();
        results1.setName("Don't show the location");
        results1.setVicinity("");
        if (results != null)
            results.add(0, results1);
        return results;
    }

    public void setResults(List<LocationBean.results> results) {
        this.results = results;
    }

    public class results implements Serializable {
        private geometry geometry;

        public LocationBean.results.geometry getGeometry() {
            return geometry;
        }

        public void setGeometry(LocationBean.results.geometry geometry) {
            this.geometry = geometry;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private String name;
        private String vicinity;

        public String getVicinity() {
            return vicinity;
        }

        public void setVicinity(String vicinity) {
            this.vicinity = vicinity;
        }

        public class geometry implements Serializable {
            private location location;

            public LocationBean.results.geometry.location getLocation() {
                return location;
            }

            public void setLocation(LocationBean.results.geometry.location location) {
                this.location = location;
            }

            public class location implements Serializable {
                private double lng;
                private double lat;

                public double getLng() {
                    return lng;
                }

                public void setLng(double lng) {
                    this.lng = lng;
                }

                public double getLat() {
                    return lat;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }
            }
        }


    }
}
