package maojian.android.walnut.home.addpost;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import maojian.android.walnut.R;
import maojian.android.walnut.utils.eventbus.AddLocationEvent;
import maojian.android.walnut.utils.eventbus.FinishActivityEvent;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Administrator on 2016/5/23.
 */
public class LocationAdapter extends BaseAdapter {
    private Context mcontext;
    private List<LocationBean.results> pois;
    private int number;
    private String location;

    public LocationAdapter(Context context, List<LocationBean.results> pois, String location1) {
        this.location = location1;
        mcontext = context;
        this.pois = pois;
        if (pois == null) {
            number = 0;
        } else {
            number = pois.size();
        }
    }

    @Override
    public int getCount() {
        return number;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        viewHolder viewholder;
        if (view == null) {
            viewholder = new viewHolder();
            view = LayoutInflater.from(mcontext).inflate(
                    R.layout.location_item, null);
            viewholder.doctorname = (TextView) view.findViewById(R.id.tv_title);
            viewholder.title = (TextView) view.findViewById(R.id.tv_name);
            viewholder.iv_check = (ImageView) view.findViewById(R.id.iv_check);
            view.setTag(viewholder);
        } else {
            viewholder = (viewHolder) view.getTag();
        }
        LocationBean.results poisBean = pois.get(i);
//        if (mMessage.get(i).getDoctorName() != null) {
        viewholder.doctorname.setText(poisBean.getName());
        if ((location.equals("") && i == 0) || location.equals(poisBean.getName())) {
            viewholder.iv_check.setImageResource(R.drawable.camera_shutter);
        } else {
            viewholder.iv_check.setImageResource(R.drawable.ssdk_oks_classic_check_default);
        }
//        }
//        if (mMessage.get(i).getHospitalName() != null) {
        if (i == 0) {
            viewholder.title.setVisibility(View.GONE);
        } else {
            viewholder.title.setVisibility(View.VISIBLE);
            viewholder.title.setText(poisBean.getVicinity());
        }

//        }
//        if (mMessage.get(i).getDeptName() != null) {
//            viewholder.doctorkemu.setText(mMessage.get(i).getDeptName());
//        }
//        if (mMessage.get(i).getDoctorTitle() != null) {
//            viewholder.doctorwork.setText(mMessage.get(i).getDoctorTitle());
//        }
//        viewholder.iv_check.setOnClickListener(new lvButtonListener(poisBean));

        return view;
    }

    public static class viewHolder {
        TextView doctorname;
        TextView title;
        ImageView iv_check;
        TextView doctorhosp;
    }

    class lvButtonListener implements View.OnClickListener {
        LocationBean.results poisBean;

        lvButtonListener(LocationBean.results poisBean1) {
            poisBean = poisBean1;
        }

        @Override
        public void onClick(View v) {
            int vid = v.getId();
            EventBus.getDefault().post(new AddLocationEvent(poisBean.getName()));
            ((Activity) mcontext).finish();
//           /* Intent intent=new Intent();
//            intent.setClass(mcontext, SearchDocTimeActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("DoctorMessage",Mmessage);
//            intent.putExtras(bundle);
//            mcontext.startActivity(intent);*/
//            mcontext.startActivity(new Intent(mcontext, WebViewActivity.class).putExtra("title", Mmessage.getDeptName()).putExtra("myurl",
//                    BaseConstant.baseWebViewUrl + URLUtils.toURLEncoded(BaseConstant.mydoctorbase + Mmessage.getHospitalId() + "&hospitalCode=" +
//                            Mmessage.getHospitalCode() + "&doctorCode=" + Mmessage.getDoctorCode() + "&branchHospitalCode=" +
//                            Mmessage.getBranchHospitalCode()+"&appCode=easyHealth"+"&deptCode="+Mmessage.getDeptCode())));
        }
    }
}
