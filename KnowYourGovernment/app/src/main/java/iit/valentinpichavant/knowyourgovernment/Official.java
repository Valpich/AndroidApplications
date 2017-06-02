package iit.valentinpichavant.knowyourgovernment;

import java.io.Serializable;

/**
 * Created by valentinpichavant on 4/1/17.
 */

public class Official implements Serializable {

    private int officialIndices;
    private String name;
    private String address;
    private String party;
    private String officePhone;
    private String officeUrl;
    private String email;
    private String photoUrl;
    private String googlePlus;
    private String facebook;
    private String twitter;
    private String youtube;

    public Official(int officialIndices, String name, String address, String party, String officePhone, String officeUrl, String email, String photoUrl, String googlePlus, String facebook, String twitter, String youtube) {
        this.officialIndices = officialIndices;
        this.name = name;
        this.address = address;
        this.party = party;
        this.officePhone = officePhone;
        this.officeUrl = officeUrl;
        this.email = email;
        this.photoUrl = photoUrl;
        this.googlePlus = googlePlus;
        this.facebook = facebook;
        this.twitter = twitter;
        this.youtube = youtube;
    }

    public int getOfficialIndices() {
        return officialIndices;
    }

    public void setOfficialIndices(int officialIndices) {
        this.officialIndices = officialIndices;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getOfficeUrl() {
        return officeUrl;
    }

    public void setOfficeUrl(String officeUrl) {
        this.officeUrl = officeUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getGooglePlus() {
        return googlePlus;
    }

    public void setGooglePlus(String googlePlus) {
        this.googlePlus = googlePlus;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    @Override
    public String toString() {
        return "Official{" +
                "officialIndices=" + officialIndices +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", party='" + party + '\'' +
                ", officePhone='" + officePhone + '\'' +
                ", officeUrl='" + officeUrl + '\'' +
                ", email='" + email + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", googlePlus='" + googlePlus + '\'' +
                ", facebook='" + facebook + '\'' +
                ", twitter='" + twitter + '\'' +
                ", youtube='" + youtube + '\'' +
                '}';
    }
}
