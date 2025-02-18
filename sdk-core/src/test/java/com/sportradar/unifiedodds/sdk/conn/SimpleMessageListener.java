/*
 * Copyright (C) Sportradar AG. See LICENSE for full license governing this code
 */

package com.sportradar.unifiedodds.sdk.conn;

import com.sportradar.unifiedodds.sdk.OddsFeed;
import com.sportradar.unifiedodds.sdk.OddsFeedListener;
import com.sportradar.unifiedodds.sdk.OddsFeedSession;
import com.sportradar.unifiedodds.sdk.entities.LongTermEvent;
import com.sportradar.unifiedodds.sdk.entities.SportEvent;
import com.sportradar.unifiedodds.sdk.oddsentities.*;
import com.sportradar.unifiedodds.sdk.shared.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * A custom feed listener implementation which outputs the data to the provided logger
 */
public class SimpleMessageListener implements OddsFeedListener {
    private final Logger logger;
    private final Locale locale = Locale.ENGLISH;
    private final Date startDate;
    private final OddsFeed oddsFeed;
    private final List<Locale> desiredLocales;
    private final String listenerVersion;
    public List<FeedMessage> FeedMessages;

    public SimpleMessageListener(String listener_version, OddsFeed oddsFeed, List<Locale> desiredLocales) {
        this.logger = LoggerFactory.getLogger(this.getClass().getName() + "-" + listener_version);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 1);
        startDate = cal.getTime();
        this.oddsFeed = oddsFeed;
        this.desiredLocales = desiredLocales;
        this.listenerVersion = listener_version;
        FeedMessages = new ArrayList<>();
    }

    /**
     * Any kind of odds update, or betstop signal results in an OddsChanges Message.
     *
     * @param sender      the session
     * @param oddsChanges the odds changes message
     */
    @Override
    public void onOddsChange(OddsFeedSession sender, OddsChange<SportEvent> oddsChanges) {
        logBaseMessageData(oddsChanges, oddsChanges.getEvent(), oddsChanges.getProducer());
        String message = String.format("[%s]: event=%s, data=%s", listenerVersion, oddsChanges.getEvent().getId(), Helper.provideCleanMsgForLog(oddsChanges.getRawMessage()));
        FeedMessages.add(new FeedMessage(oddsChanges.getTimestamps().getCreated(), oddsChanges.getEvent(), "oddsChange"));
        Helper.writeToOutput("Called event onOddsChange: " + message);
    }

    /**
     * If there are important fixture updates you will receive fixturechange message. The thinking
     * is that most fixture updates are queried by you yourself using the SportInfoManager. However,
     * if there are important/urgent changes you will also receive a fixture change message (e.g. if
     * a match gets delayed, or if Sportradar for some reason needs to stop live coverage of a match
     * etc.). This message allows you to promptly respond to such changes
     *
     * @param sender        the session
     * @param fixtureChange the SDKFixtureChange message - describing what sport event and what type
     *                      of fixture change
     */
    @Override
    public void onFixtureChange(OddsFeedSession sender, FixtureChange<SportEvent> fixtureChange) {
        logBaseMessageData(fixtureChange, fixtureChange.getEvent(), fixtureChange.getProducer());
        String message = String.format("[%s]: event=%s, data=%s", listenerVersion, fixtureChange.getEvent().getId(), Helper.provideCleanMsgForLog(fixtureChange.getRawMessage()));
        FeedMessages.add(new FeedMessage(fixtureChange.getTimestamps().getCreated(), fixtureChange.getEvent(), "fixtureChange"));
        Helper.writeToOutput("Called event onFixtureChange: " + message);
    }

    /**
     * Send to rapidly suspend a set of markets (often all)
     *
     * @param sender  the session
     * @param betStop the betstop message
     */
    @Override
    public void onBetStop(OddsFeedSession sender, BetStop<SportEvent> betStop) {
        logBaseMessageData(betStop, betStop.getEvent(), betStop.getProducer());
        String message = String.format("[%s]: event=%s, data=%s", listenerVersion, betStop.getEvent().getId(), Helper.provideCleanMsgForLog(betStop.getRawMessage()));
        FeedMessages.add(new FeedMessage(betStop.getTimestamps().getCreated(), betStop.getEvent(), "betStop"));
        Helper.writeToOutput("Called event onBetStop: " + message);
    }

    /**
     * The onBetSettlement callback is received whenever a BetSettlement message is received. It
     * contains information about what markets that should be settled how. All markets and outcomes
     * that you have received odds changes messages for at some point in time you will receive
     * betsettlement messages for at some later point in time. That is if you receive odds for
     * outcome X for market Y, you will at a later time receive a BetSettlement message that
     * includes outcome X for market Y.
     *
     * @param sender    the session
     * @param betSettlement the BetSettlement message
     */
    @Override
    public void onBetSettlement(OddsFeedSession sender, BetSettlement<SportEvent> betSettlement) {
        logBaseMessageData(betSettlement, betSettlement.getEvent(), betSettlement.getProducer());
        String message = String.format("[%s]: event=%s, data=%s", listenerVersion, betSettlement.getEvent().getId(), Helper.provideCleanMsgForLog(betSettlement.getRawMessage()));
        FeedMessages.add(new FeedMessage(betSettlement.getTimestamps().getCreated(), betSettlement.getEvent(), "betSettlement"));
        Helper.writeToOutput("Called event onBetSettlement: " + message);
    }

    /**
     * If the markets were cancelled you may receive a
     * {@link BetCancel} describing which markets were
     * cancelled
     *
     * @param sender    the session
     * @param betCancel A {@link BetCancel} instance
     *                  specifying which markets were cancelled
     */
    @Override
    public void onBetCancel(OddsFeedSession sender, BetCancel<SportEvent> betCancel) {
        logBaseMessageData(betCancel, betCancel.getEvent(), betCancel.getProducer());
        String message = String.format("[%s]: event=%s, data=%s", listenerVersion, betCancel.getEvent().getId(), Helper.provideCleanMsgForLog(betCancel.getRawMessage()));
        FeedMessages.add(new FeedMessage(betCancel.getTimestamps().getCreated(), betCancel.getEvent(), "betCancel"));
        Helper.writeToOutput("Called event onBetCancel: " + message);
    }

    /**
     * If the bet cancellations were send in error you may receive a
     * {@link RollbackBetCancel} describing the
     * erroneous cancellations
     *
     * @param sender      the session
     * @param rollbackBetCancel A {@link RollbackBetCancel}
     *                    specifying erroneous cancellations
     */
    @Override
    public void onRollbackBetCancel(OddsFeedSession sender, RollbackBetCancel<SportEvent> rollbackBetCancel) {
        logBaseMessageData(rollbackBetCancel, rollbackBetCancel.getEvent(), rollbackBetCancel.getProducer());
        String message = String.format("[%s]: event=%s, data=%s", listenerVersion, rollbackBetCancel.getEvent().getId(), Helper.provideCleanMsgForLog(rollbackBetCancel.getRawMessage()));
        FeedMessages.add(new FeedMessage(rollbackBetCancel.getTimestamps().getCreated(), rollbackBetCancel.getEvent(), "rollbackBetCancel"));
        Helper.writeToOutput("Called event onRollbackBetCancel: " + message);
    }

    /**
     * If a BetSettlement was generated in error, you may receive a RollbackBetsettlement and have
     * to try to do whatever you can to undo the BetSettlement if possible.
     *
     * @param sender                the session
     * @param rollbackBetSettlement the rollbackBetSettlement message referring to a previous
     *                              BetSettlement
     */
    @Override
    public void onRollbackBetSettlement(OddsFeedSession sender, RollbackBetSettlement<SportEvent> rollbackBetSettlement) {
        logBaseMessageData(rollbackBetSettlement, rollbackBetSettlement.getEvent(), rollbackBetSettlement.getProducer());
        String message = String.format("[%s]: event=%s, data=%s", listenerVersion, rollbackBetSettlement.getEvent().getId(), Helper.provideCleanMsgForLog(rollbackBetSettlement.getRawMessage()));
        FeedMessages.add(new FeedMessage(rollbackBetSettlement.getTimestamps().getCreated(), rollbackBetSettlement.getEvent(), "rollbackBetSettlement"));
        Helper.writeToOutput("Called event onRollbackBetSettlement: " + message);
    }

    /**
     * This handler is called when the SDK detects that it has problems parsing a certain message.
     * The handler can decide to take some custom action (shutting down everything etc. doing some
     * special analysis of the raw message content etc) or just ignore the message. The SDK itself
     * will always log that it has received an unparseable message and will ignore the message so a
     * typical implementation can leave this handler empty.
     *
     * @param sender     the session
     * @param rawMessage the raw message received from Betradar
     * @param event      if the SDK was able to extract the event this message is for it will be here
     *                   otherwise null
     * @deprecated in favour of {{@link #onUnparsableMessage(OddsFeedSession, UnparsableMessage)}} from v2.0.11
     */
    @Override
    @Deprecated
    public void onUnparseableMessage(OddsFeedSession sender, byte[] rawMessage, SportEvent event) {
        if (event != null) {
            logger.info("Problems deserializing received message for event " + event.getId());
        } else {
            logger.info("Problems deserializing received message"); // probably a system message deserialization failure
        }
    }

    /**
     * This handler is called when the SDK detects that it has problems parsing/dispatching a message.
     * The handler can decide to take some custom action (shutting down everything etc. doing some
     * special analysis of the raw message content etc) or just ignore the message. The SDK itself
     * will always log that it has received an unparseable message.
     *
     * @param sender            the session
     * @param unparsableMessage A {@link UnparsableMessage} instance describing the message that had issues
     * @since v2.0.11
     */
    @Override
    public void onUnparsableMessage(OddsFeedSession sender, UnparsableMessage unparsableMessage) {
        Producer possibleProducer = unparsableMessage.getProducer(); // the SDK will try to provide the origin of the message
        String message = String.format("[%s]: event=%s, data=%s", listenerVersion, unparsableMessage.getEvent().getId(), Helper.provideCleanMsgForLog(unparsableMessage.getRawMessage()));
        FeedMessages.add(new FeedMessage(unparsableMessage.getTimestamps().getCreated(), unparsableMessage.getEvent(), "unparsableMessage"));
        Helper.writeToOutput("Called event onUnparsableMessage: " + message);

        if (unparsableMessage.getEvent() != null) {
            String xml = new String(unparsableMessage.getRawMessage());
            logger.info("Problems detected on received message for event " + unparsableMessage.getEvent().getId() + ". Message: " + xml);

        } else {
            logger.info("Problems detected on received message"); // probably a system message deserialization failure
        }
    }

    private void logBaseMessageData(Message message, SportEvent event, Producer producer) {
        logger.info("Received " + message.getClass().getSimpleName() + " for producer: " + producer.getId() + "-" + producer.getName() + " for sportEvent: " + event.getId());

        String sb = new StringBuilder()
        .append("Timestamps for ").append(message.getClass().getSimpleName())
        .append(": [Timestamp=").append(message.getTimestamp()).append("=").append(new Date(message.getTimestamp()))
        .append("; GeneratedAt=").append(message.getTimestamps().getCreated()).append("=").append(new Date(message.getTimestamps().getCreated()))
        .append("; SentAt=").append(message.getTimestamps().getSent()).append("=").append(new Date(message.getTimestamps().getSent()))
        .append("; ReceivedAt=").append(message.getTimestamps().getReceived()).append("=").append(new Date(message.getTimestamps().getReceived()))
        .append("; Dispatched=").append(message.getTimestamps().getDispatched()).append("=").append(new Date(message.getTimestamps().getDispatched()))
                .toString();

        if (message.getTimestamps().getSent() == 0)
        {
            logger.error("Message {} created {} on producer {}-{} does not have sent timestamp.", message.getClass().getSimpleName(), message.getTimestamps().getCreated(), message.getProducer().getId(), message.getProducer().getName());
            String x = "break";
        }

        logger.info(sb);
    }


    private boolean shouldContinue(SportEvent sportEvent)
    {
        if (sportEvent instanceof LongTermEvent)
        {
            LongTermEvent longTermEvent = (LongTermEvent) sportEvent;
            logger.info("LongTermEvent {} {} - Name: {}",
                        longTermEvent.getClass().getSimpleName(),
                        longTermEvent.getId(),
                        longTermEvent.getName(locale));
            return true;
        }
        if(sportEvent.getSportId().getId() == 2){
            return true;
        }

        return false;
    }
}
