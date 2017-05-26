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
    private result result;

    public LocationBean.result getResult() {
        return result;
    }

    public void setResult(LocationBean.result result) {
        this.result = result;
    }

    public class result implements Serializable {
    private location location;
    private List<pois> pois;

        public List<LocationBean.result.pois> getPois() {
            return pois;
        }

        public void setPois(List<LocationBean.result.pois> pois) {
            this.pois = pois;
        }

        public LocationBean.result.location getLocation() {
        return location;
    }

    public void setLocation(LocationBean.result.location location) {
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

    public class pois implements Serializable {
        private String addr;
        private String name;

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public LocationBean.result.pois.point getPoint() {
            return point;
        }

        public void setPoint(LocationBean.result.pois.point point) {
            this.point = point;
        }

        private point point;

        public class point implements Serializable {
            private double x;
            private double y;

            public double getX() {
                return x;
            }

            public void setX(double x) {
                this.x = x;
            }

            public double getY() {
                return y;
            }

            public void setY(double y) {
                this.y = y;
            }
        }
    }
    }
}
