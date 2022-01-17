package me.declipsonator.meteortweaks.utils;


public class GithubReleaseJson {
    String name;
    String tag_name;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getTagName() {
        return tag_name;
    }
    public void setTagName(String tagName) {
        tag_name = tagName;
    }
    @Override
    public String toString() {
        return "Module {name=" + name + ", tag_name=" + tag_name + "}";
    }
}
