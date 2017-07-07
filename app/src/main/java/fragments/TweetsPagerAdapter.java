package fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by rafasj6 on 7/3/17.
 */

public class TweetsPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = new String[] {"Home", "Mentions"};
    private Context context;
    public HomeTimelineFragment homeTimelineFragment;
    public MentionsTimelineFragment mentionsTimelineFragment;

    public TweetsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position){
        if (position == 0)
        {
            homeTimelineFragment = getHomeTimeline();
            return homeTimelineFragment;
        }
        else if(position == 1){
             mentionsTimelineFragment = getMentionsTimelineFragment();
            return mentionsTimelineFragment;
        }else{
            return null;
        }

    }
    private HomeTimelineFragment getHomeTimeline(){
        if(homeTimelineFragment == null){
            return new HomeTimelineFragment();
        }
        else{
            return homeTimelineFragment;
        }


    }

    private MentionsTimelineFragment getMentionsTimelineFragment(){
        if(mentionsTimelineFragment == null){
            return new MentionsTimelineFragment();
        }
        else{
            return mentionsTimelineFragment;
        }


    }



    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position] ;
    }
}
