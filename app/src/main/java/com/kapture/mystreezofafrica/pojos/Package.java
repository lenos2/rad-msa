
package com.kapture.mystreezofafrica.pojos;


@SuppressWarnings("unused")
public class Package {

    private String mImage;
    private String mItinerary;
    private String mName;
    private String mTour;
    private String mPricing;

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public String getItinerary() {
        return mItinerary;
    }

    public void setItinerary(String itinerary) {
        mItinerary = itinerary;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getTour() {
        return mTour;
    }

    public void setTour(String packageType) {
        mTour = packageType;
    }

    public String getPricing() {
        return mPricing;
    }

    public void setPricing(String pricing) {
        mPricing = pricing;
    }

}
