package com.sportradar.unifiedodds.sdk.caching.exportable;

import java.io.Serializable;
import java.util.List;

public class ExportableProducerInfoCI implements Serializable {
    private boolean isAutoTraded;
    private boolean isInHostedStatistics;
    private boolean isInLiveCenterSoccer;
    private boolean isInLiveScore;
    private List<ExportableProducerInfoLinkCI> producerInfoLinks;
    private List<ExportableStreamingChannelCI> streamingChannels;

    public ExportableProducerInfoCI(boolean isAutoTraded, boolean isInHostedStatistics, boolean isInLiveCenterSoccer, boolean isInLiveScore, List<ExportableProducerInfoLinkCI> producerInfoLinks, List<ExportableStreamingChannelCI> streamingChannels) {
        this.isAutoTraded = isAutoTraded;
        this.isInHostedStatistics = isInHostedStatistics;
        this.isInLiveCenterSoccer = isInLiveCenterSoccer;
        this.isInLiveScore = isInLiveScore;
        this.producerInfoLinks = producerInfoLinks;
        this.streamingChannels = streamingChannels;
    }

    public boolean isAutoTraded() {
        return isAutoTraded;
    }

    public void setAutoTraded(boolean autoTraded) {
        isAutoTraded = autoTraded;
    }

    public boolean isInHostedStatistics() {
        return isInHostedStatistics;
    }

    public void setInHostedStatistics(boolean inHostedStatistics) {
        isInHostedStatistics = inHostedStatistics;
    }

    public boolean isInLiveCenterSoccer() {
        return isInLiveCenterSoccer;
    }

    public void setInLiveCenterSoccer(boolean inLiveCenterSoccer) {
        isInLiveCenterSoccer = inLiveCenterSoccer;
    }

    public boolean isInLiveScore() {
        return isInLiveScore;
    }

    public void setInLiveScore(boolean inLiveScore) {
        isInLiveScore = inLiveScore;
    }

    public List<ExportableProducerInfoLinkCI> getProducerInfoLinks() {
        return producerInfoLinks;
    }

    public void setProducerInfoLinks(List<ExportableProducerInfoLinkCI> producerInfoLinks) {
        this.producerInfoLinks = producerInfoLinks;
    }

    public List<ExportableStreamingChannelCI> getStreamingChannels() {
        return streamingChannels;
    }

    public void setStreamingChannels(List<ExportableStreamingChannelCI> streamingChannels) {
        this.streamingChannels = streamingChannels;
    }
}
