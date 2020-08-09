package com.example.http1;

import java.util.List;

public class LessionResult {
    private  int mStatus;
    private List<Lesson> mLessons;
    public void setLessons(List<Lesson> lessons) {
        mLessons = lessons;
    }

    @Override
    public String toString() {
        return "LessionResult{" +
                "mStatus=" + mStatus +
                ", mLessons=" + mLessons +
                '}';
    }

    public  static  class  Lesson{
        private  int mId;
        private  int mLearner;
        private  String mName;
        private  String mPicSmall;
        private  String mPicBig;
        private String mDescription;


        @Override
        public String toString() {
            return "Lesson{" +
                    "mId=" + mId +
                    ", mLearner=" + mLearner +
                    ", mName='" + mName + '\'' +
                    ", mPicSmall='" + mPicSmall + '\'' +
                    ", mPicBig='" + mPicBig + '\'' +
                    ", mDescription='" + mDescription + '\'' +
                    '}';
        }

        public int getId() {
            return mId;
        }

        public void setId(int id) {
            mId = id;
        }

        public int getLearner() {
            return mLearner;
        }

        public void setLearner(int learner) {
            mLearner = learner;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

        public String getPicSmall() {
            return mPicSmall;
        }

        public void setPicSmall(String picSmall) {
            mPicSmall = picSmall;
        }

        public String getPicBig() {
            return mPicBig;
        }

        public void setPicBig(String picBig) {
            mPicBig = picBig;
        }

        public String getDescription() {
            return mDescription;
        }

        public void setDescription(String description) {
            mDescription = description;
        }

    }
}
