package ru.geekbrains.evgeniy.itunesplayer;

// created by http://www.jsonschema2pojo.org/

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelResponse {

    @SerializedName("resultCount")
    @Expose
    private Integer resultCount;
    @SerializedName("results")
    @Expose
    private List<ModelResponceResult> results = null;

    public Integer getResultCount() {
        return resultCount;
    }

    public void setResultCount(Integer resultCount) {
        this.resultCount = resultCount;
    }

    public List<ModelResponceResult> getResults() {
        return results;
    }

    public void setResults(List<ModelResponceResult> results) {
        this.results = results;
    }

}