package ie.tcd.cs7is3.model;

public class TopicsObject {
    private String topicNum;
    private String topicTitle;
    private String topicDesc;
    private String topicNarrative;


    public TopicsObject(String topicNum, String topicTitle, String topicDesc, String topicNarrative) {
        this.topicNum = topicNum;
        this.topicTitle = topicTitle;
        this.topicDesc = topicDesc;
        this.topicNarrative = topicNarrative;
    }


    public String getTopicNum() {
        return topicNum;
    }


    public void setTopicNum(String topicNum) {
        this.topicNum = topicNum;
    }


    public String getTopicTitle() {
        return topicTitle;
    }


    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }


    public String getTopicDesc() {
        return topicDesc;
    }


    public void setTopicDesc(String topicDesc) {
        this.topicDesc = topicDesc;
    }


    public String getTopicNarrative() {
        return topicNarrative;
    }


    public void setTopicNarrative(String topicNarrative) {
        this.topicNarrative = topicNarrative;
    }

    

}
