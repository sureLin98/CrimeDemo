package com.demo.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.DateFormat;
import java.util.List;

public class CrimeListFragment extends Fragment {

        private RecyclerView mCrimeRecyclerView;
        private CrimeListAdapter mAdapter;
        private TextView noCrimeText;
        private boolean mSubtitleVisible;
        private final String SUBTITLE_VISIBLE_KEY="subtitle";

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

                View v=inflater.inflate(R.layout.fragment_crime_list,container,false);

                mCrimeRecyclerView=v.findViewById(R.id.crime_recycle_view);
                noCrimeText=v.findViewById(R.id.no_crime_text);
                mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                if(savedInstanceState!=null){
                        mSubtitleVisible=savedInstanceState.getBoolean(SUBTITLE_VISIBLE_KEY);
                }

                setHasOptionsMenu(true);
                updateUI();

                return v;
        }

        @Override
        public void onResume() {
                super.onResume();
                updateUI();
        }

        @Override
        public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
                super.onCreateOptionsMenu(menu, inflater);
                inflater.inflate(R.menu.fragment_crime_list,menu);
                MenuItem subtitleItem=menu.findItem(R.id.show_subtitle);
                if(mSubtitleVisible){
                        subtitleItem.setTitle(R.string.hide_subtitle);
                }else {
                        subtitleItem.setTitle(R.string.show_subtitle);
                }
        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                        case R.id.new_crime:
                                Crime crime=new Crime();
                                CrimeLab.get(getActivity()).addCrime(crime);
                                Intent intent=CrimePagerActivity.newIntent(getActivity(),crime.getId());
                                startActivity(intent);
                                return true;
                        case R.id.show_subtitle:
                                mSubtitleVisible=!mSubtitleVisible;
                                getActivity().invalidateOptionsMenu();
                                updateSubtitle();
                                return true;
                        default:
                                return super.onOptionsItemSelected(item);
                }

        }

        @Override
        public void onSaveInstanceState(@NonNull Bundle outState) {
                super.onSaveInstanceState(outState);
                outState.putBoolean(SUBTITLE_VISIBLE_KEY,mSubtitleVisible);
        }

        private void updateUI(){
                CrimeLab crimeLab=CrimeLab.get(getActivity());
                List<Crime> crimes=crimeLab.getCrimes();

                if(mAdapter==null){
                        mAdapter=new CrimeListAdapter(crimes);
                        mCrimeRecyclerView.setAdapter(mAdapter);
                }else {
                        mAdapter.setCrimes(checkCrimeIsNull(crimes));
                        mAdapter.notifyDataSetChanged();
                }

                updateSubtitle();
                setRecyclerViewVisible();
        }

        public void updateSubtitle(){
                String subtitle;
                CrimeLab crimeLab=CrimeLab.get(getActivity());
                int crimeCount=crimeLab.getCrimes().size();
                if(crimeCount>1){
                        subtitle=crimeCount+" crimes";
                }else {
                        subtitle=crimeCount+" crime";
                }

                if(!mSubtitleVisible){
                        subtitle=null;
                }
                AppCompatActivity activity=(AppCompatActivity)getActivity();
                activity.getSupportActionBar().setSubtitle(subtitle);
        }

        public void setRecyclerViewVisible(){
                if(CrimeLab.get(getActivity()).getCrimes().size()==0){
                        mCrimeRecyclerView.setVisibility(View.INVISIBLE);
                        noCrimeText.setVisibility(View.VISIBLE);
                }else {
                        mCrimeRecyclerView.setVisibility(View.VISIBLE);
                        noCrimeText.setVisibility(View.GONE);
                }
        }

        public List<Crime> checkCrimeIsNull(List<Crime> crimes){
                for(int i=0;i<crimes.size();i++){
                        if(crimes.get(i).getTitle().equals("")){
                                CrimeLab.get(getActivity()).deleteCrime(i);
                                crimes.remove(i);
                        }
                }
                return crimes;
        }

        private class CrimeHoder extends RecyclerView.ViewHolder implements View.OnClickListener {

                private TextView mTitleTextView;
                private TextView mDateTextView;
                private ImageView mSolvedImageView;
                private Crime mCrime;

                public CrimeHoder(LayoutInflater inflater,ViewGroup parent){
                        super(inflater.inflate(R.layout.list_item_crime,parent,false));
                        itemView.setOnClickListener(this);
                        mTitleTextView=itemView.findViewById(R.id.crime_title);
                        mDateTextView=itemView.findViewById(R.id.crime_date);
                        mSolvedImageView=itemView.findViewById(R.id.crime_solved);
                }

                public void bind(Crime crime){
                        mCrime=crime;
                        mTitleTextView.setText(mCrime.getTitle());
                        mDateTextView.setText(DateFormat.getDateInstance(DateFormat.FULL).format(mCrime.getDate()));
                        mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.INVISIBLE);
                }

                @Override
                public void onClick(View v) {
                        Intent intent=CrimePagerActivity.newIntent(getActivity(),mCrime.getId());
                        startActivity(intent);
                }
        }

        private class CrimeListAdapter extends RecyclerView.Adapter<CrimeHoder>{
                private List<Crime> mCrimes;

                public CrimeListAdapter(List<Crime> crimes){
                        mCrimes=crimes;
                }

                @NonNull
                @Override
                public CrimeHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        LayoutInflater inflater=LayoutInflater.from(getActivity());
                        return new CrimeHoder(inflater,parent);
                }

                @Override
                public void onBindViewHolder(@NonNull CrimeHoder holder, int position) {
                        Crime crime=mCrimes.get(position);
                        holder.bind(crime);
                }

                @Override
                public int getItemCount() {
                        return mCrimes.size();
                }

                public void setCrimes(List<Crime> crimes){
                        mCrimes=crimes;
                }
        }

}
