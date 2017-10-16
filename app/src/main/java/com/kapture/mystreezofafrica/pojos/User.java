
package com.kapture.mystreezofafrica.pojos;


@SuppressWarnings("unused")
public class User {

    private String mDob;
    private String mEmail;
    private String mGender;
    private String mName;
    private String mProfilePic;

    public String getDob() {
        return mDob;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getGender() {
        return mGender;
    }

    public String getName() {
        return mName;
    }

    public String getProfilePic() {
        return mProfilePic;
    }

    public static class Builder {

        private String mDob;
        private String mEmail;
        private String mGender;
        private String mName;
        private String mProfilePic;

        public User.Builder withDob(String dob) {
            mDob = dob;
            return this;
        }

        public User.Builder withEmail(String email) {
            mEmail = email;
            return this;
        }

        public User.Builder withGender(String gender) {
            mGender = gender;
            return this;
        }

        public User.Builder withName(String name) {
            mName = name;
            return this;
        }

        public User.Builder withProfilePic(String profilePic) {
            mProfilePic = profilePic;
            return this;
        }

        public User build() {
            User User = new User();
            User.mDob = mDob;
            User.mEmail = mEmail;
            User.mGender = mGender;
            User.mName = mName;
            User.mProfilePic = mProfilePic;
            return User;
        }

    }

}
