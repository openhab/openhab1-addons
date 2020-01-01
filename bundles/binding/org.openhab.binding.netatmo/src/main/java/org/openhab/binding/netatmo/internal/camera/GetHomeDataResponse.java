/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.netatmo.internal.camera;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.openhab.binding.netatmo.internal.messages.AbstractMessagePart;
import org.openhab.binding.netatmo.internal.messages.AbstractResponse;

/**
 * Java Bean to represent a JSON response to a <code>gethomedata</code> API
 * method call.
 * <p>
 * Sample response:
 *
 * <pre>
 *  {
 * "body":  {
 *   "homes":  [
 *     {
 *       "id":  "55817b8da88d1fb55d8b4689",
 *       "name":  "Home Tom",
 *       "place":  {
 *         "country":  "SE",
 *         "timezone":  "Europe/Stockholm"
 *       },
 *       "persons":  [
 *          {
 *           "id":  "0c08eedb-cc6b-4299-8b8f-1fbb939536c8",
 *            "last_seen":  1441110023,
 *           "out_of_sight":  false,
 *            "face":  {
 *              "id":  "55e598250a6e6e6170cf7cfb",
 *              "version":  1,
 *             "key":  "f06de130500990d5286a11d9b9c83eeaa530d984b0a2f6bca433472827f3f25c"
 *           }
 *         },
 *         {
 *           "id":  "0e52b4da-2d5a-42f5-be6e-85d8db7adfe8",
 *           "last_seen":  1441112288,
 *           "out_of_sight":  false,
 *           "face":  {
 *             "id":  "55e5a1240a6e6e5e70cf7e0a",
 *             "version":  1,
 *             "key":  "2c24b42ee7b87a47d16a046a6d0c0821523c5474b30c0b142ffc3510beddd67f"
 *           }
 *         },
 *         {
 *           "id":  "13e843a4-2bcb-4d52-855c-1c5570663f76",
 *           "last_seen":  1441114497,
 *           "out_of_sight":  false,
 *           "face":  {
 *             "id":  "55e5a98d0a6e6e6170cf7d4c",
 *             "version":  1,
 *             "key":  "42d2c1dd175c9128b2188537551680c3fc2c8d14eb1126c82342007db6b52848"
 *           }
 *         },
 *         {
 *           "id":  "3550700b-36cd-4c92-8b97-b64dba19f62e",
 *           "last_seen":  1441120682,
 *           "out_of_sight":  false,
 *           "face":  {
 *             "id":  "559b9b3f0a6e6ed8668b466b",
 *             "key":  "311e4ca310587a1ac0ffb2f31e7d2fc99ef5f7a7ca85cbe59242aaafacd82c25",
 *             "version":  1
 *           },
 *           "pseudo":  "Tom"
 *         }
 *       ],
 *       "events":  [
 *          {
 *           "id":  "55e5c1c50a6e6e5f70cf7e4e",
 *           "type":  "person",
 *           "time":  1441120682,
 *           "camera_id":  "70:ee:50:13:8b:35",
 *           "person_id":  "3550700b-36cd-4c92-8b97-b64dba19f62e",
 *           "snapshot":  {
 *             "id":  "55e5c1c50a6e6e5f70cf7e50",
 *             "version":  1,
 *             "key":  "8bbbe58c7b4fba061e8c29c2b7a57e6d16956b06c6ebd992e01267e755f7df72"
 *           },
 *           "is_arrival":  false,
 *           "message":  "Tom seen"
 *         }
 *       ],
 *       "cameras":  [
 *         {
 *           "id":  "70:ee:50:13:8b:35",
 *           "status":  "on",
 *           "sd_status":  "on",
 *           "alim_status":  "on",
 *           "name":  "Tom's"
 *         },
 *         {
 *            "id":  "70:ee:50:13:eb:35",
 *           "status":  "disconnected",
 *           "sd_status":  "on",
 *           "alim_status":  "on"
 *         }
 *       ]
 *     }
 *   ],
 *   "user":  {
 *     "reg_locale":  "en-US",
 *     "lang":  "en-US"
 *   }
 * },
 * "status": "ok",
 * "time_exec":  0.015316009521484,
 * "time_server":  1441120797
 *}
 * </pre>
 *
 * @author Ing. Peter Weiss
 * @since 1.8.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetHomeDataResponse extends AbstractResponse {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body extends AbstractMessagePart {
        private List<Home> homes;
        private User user;

        /**
         * "homes": [
         * ...
         * ],
         */
        @JsonProperty("homes")
        public List<Home> getHomes() {
            return this.homes;
        }

        /**
         * "user": {
         * "reg_locale": "en-US",
         * "lang": "en-US"
         */
        @JsonProperty("user")
        public User getUser() {
            return user;
        }

        @Override
        public String toString() {
            final ToStringBuilder builder = createToStringBuilder();
            builder.appendSuper(super.toString());
            builder.append("homes", this.homes);
            builder.append("user", this.user);

            return builder.toString();
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Home extends AbstractMessagePart {
        private String id;
        private String name;
        private Place place;
        private List<Person> persons;
        private List<Event> events;
        private List<Camera> cameras;

        /**
         * "id": "55817b8da88d1fb55d8b4689",
         */
        @JsonProperty("id")
        public String getId() {
            return id;
        }

        /**
         * "name": "Home Tom",
         */
        @JsonProperty("name")
        public String getName() {
            return name;
        }

        /**
         * "place": {
         * "country": "SE",
         * "timezone": "Europe/Stockholm"
         * },
         */
        @JsonProperty("place")
        public Place getPlace() {
            return place;
        }

        /**
         * "persons": [
         * ...
         * ],
         */
        @JsonProperty("persons")
        public List<Person> getPersons() {
            return persons;
        }

        /**
         * "events": [
         * ...
         * ],
         */
        @JsonProperty("events")
        public List<Event> getEvents() {
            return events;
        }

        /**
         * "cameras": [
         * ...
         * ],
         */
        @JsonProperty("cameras")
        public List<Camera> getCameras() {
            return cameras;
        }

        @Override
        public String toString() {
            final ToStringBuilder builder = createToStringBuilder();
            builder.appendSuper(super.toString());
            builder.append("id", this.id);
            builder.append("name", this.name);
            builder.append("place", this.place);
            builder.append("persons", this.persons);
            builder.append("events", this.events);
            builder.append("cameras", this.cameras);

            return builder.toString();
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Place extends AbstractMessagePart {
        private String country;
        private String timezone;

        /**
         * "country": "SE",
         */
        @JsonProperty("country")
        public String getCountry() {
            return this.country;
        }

        /**
         * "timezone": "Europe/Stockholm"
         */
        @JsonProperty("timezone")
        public String getTimezone() {
            return this.timezone;
        }

        @Override
        public String toString() {
            final ToStringBuilder builder = createToStringBuilder();
            builder.appendSuper(super.toString());
            builder.append("country", this.country);
            builder.append("timezone", this.timezone);

            return builder.toString();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Person extends AbstractMessagePart {
        private String id;
        private Date lastSeen;
        private Boolean out_of_sight;
        private Face face;
        private String pseudo;

        /**
         * "id": "0c08eedb-cc6b-4299-8b8f-1fbb939536c8",
         */
        @JsonProperty("id")
        public String getId() {
            return id;
        }

        /**
         * "last_seen": 1441110023,
         */
        @JsonProperty("last_seen")
        public Date getLastSeen() {
            return lastSeen;
        }

        /**
         * "out_of_sight": false,
         */
        @JsonProperty("out_of_sight")
        public Boolean getOut_of_sight() {
            return out_of_sight;
        }

        /**
         * "face": {
         * "id": "55e598250a6e6e6170cf7cfb",
         * "version": 1,
         * "key": "f06de130500990d5286a11d9b9c83eeaa530d984b0a2f6bca433472827f3f25c"
         * }
         */
        @JsonProperty("face")
        public Face getFace() {
            return face;
        }

        /**
         * "pseudo": "Tom"
         */
        @JsonProperty("pseudo")
        public String getPseudo() {
            return pseudo;
        }

        @Override
        public String toString() {
            final ToStringBuilder builder = createToStringBuilder();
            builder.appendSuper(super.toString());
            builder.append("id", this.id);
            builder.append("lastSeen", this.lastSeen);
            builder.append("out_of_sight", this.out_of_sight);
            builder.append("face", this.face);
            builder.append("pseudo", this.pseudo);

            return builder.toString();
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Face extends AbstractMessagePart {
        private String id;
        private Integer version;
        private String key;

        /**
         * "id": "55e598250a6e6e6170cf7cfb",
         */
        @JsonProperty("id")
        public String getId() {
            return id;
        }

        /**
         * "version": 1,
         */
        @JsonProperty("version")
        public Integer getVersion() {
            return version;
        }

        /**
         * "key": "f06de130500990d5286a11d9b9c83eeaa530d984b0a2f6bca433472827f3f25c"
         */
        @JsonProperty("key")
        public String getKey() {
            return key;
        }

        @Override
        public String toString() {
            final ToStringBuilder builder = createToStringBuilder();
            builder.appendSuper(super.toString());
            builder.append("id", this.id);
            builder.append("version", this.version);
            builder.append("key", this.key);

            return builder.toString();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Event extends AbstractMessagePart {
        private String id;
        private String type;
        private Date time;
        private String camera_id;
        private String person_id;
        private Snapshot snapshot;
        private Boolean is_arrival;
        private String message;

        /**
         * "id": "55e5c1c50a6e6e5f70cf7e4e",
         */
        @JsonProperty("id")
        public String getId() {
            return id;
        }

        /**
         * "type": "person",
         */
        @JsonProperty("type")
        public String getType() {
            return type;
        }

        /**
         * "time": 1441120682,
         */
        @JsonProperty("time")
        public Date getTime() {
            return time;
        }

        /**
         * "camera_id": "70:ee:50:13:8b:35",
         */
        @JsonProperty("camera_id")
        public String getCamera_id() {
            return camera_id;
        }

        /**
         * "person_id": "3550700b-36cd-4c92-8b97-b64dba19f62e",
         */
        @JsonProperty("person_id")
        public String getPerson_id() {
            return person_id;
        }

        /**
         * "snapshot": {
         * "id": "55e5c1c50a6e6e5f70cf7e50",
         * "version": 1,
         * "key": "8bbbe58c7b4fba061e8c29c2b7a57e6d16956b06c6ebd992e01267e755f7df72"
         * },
         */
        @JsonProperty("snapshot")
        public Snapshot getSnapshot() {
            return snapshot;
        }

        /**
         * "is_arrival": false,
         */
        @JsonProperty("is_arrival")
        public Boolean getIs_arrival() {
            return is_arrival;
        }

        /**
         * "message": "Tom seen"
         */
        @JsonProperty("message")
        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            final ToStringBuilder builder = createToStringBuilder();
            builder.appendSuper(super.toString());
            builder.append("id", this.id);
            builder.append("type", this.type);
            builder.append("time", this.time);
            builder.append("camera_id", this.camera_id);
            builder.append("person_id", this.person_id);
            builder.append("snapshot", this.snapshot);
            builder.append("is_arrival", this.is_arrival);
            builder.append("message", this.message);

            return builder.toString();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Snapshot extends AbstractMessagePart {
        private String id;
        private Integer version;
        private String key;

        /**
         * "id": "55e5c1c50a6e6e5f70cf7e50",
         */
        @JsonProperty("id")
        public String getId() {
            return id;
        }

        /**
         * "version": 1,
         */
        @JsonProperty("version")
        public Integer getVersion() {
            return version;
        }

        /**
         * "key": "8bbbe58c7b4fba061e8c29c2b7a57e6d16956b06c6ebd992e01267e755f7df72"
         */
        @JsonProperty("key")
        public String getKey() {
            return key;
        }

        @Override
        public String toString() {
            final ToStringBuilder builder = createToStringBuilder();
            builder.appendSuper(super.toString());
            builder.append("id", this.id);
            builder.append("version", this.version);
            builder.append("key", this.key);

            return builder.toString();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Camera extends AbstractMessagePart {
        private String id;
        private String status;
        private String sd_status;
        private String alim_status;
        private String name;

        /**
         * "id": "70:ee:50:13:8b:35",
         */
        @JsonProperty("id")
        public String getId() {
            return id;
        }

        /**
         * "status": "on",
         */
        @JsonProperty("status")
        public String getStatus() {
            return status;
        }

        /**
         * "sd_status": "on",
         */
        @JsonProperty("sd_status")
        public String getSd_status() {
            return sd_status;
        }

        /**
         * "alim_status": "on",
         */
        @JsonProperty("alim_status")
        public String getAlim_status() {
            return alim_status;
        }

        /**
         * "name": "Tom's"
         */
        @JsonProperty("name")
        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            final ToStringBuilder builder = createToStringBuilder();
            builder.appendSuper(super.toString());
            builder.append("id", this.id);
            builder.append("status", this.status);
            builder.append("sd_status", this.sd_status);
            builder.append("alim_status", this.alim_status);
            builder.append("name", this.name);

            return builder.toString();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User extends AbstractMessagePart {
        private String reg_locale;
        private String lang;

        /**
         * "reg_locale": "en-US",
         */
        @JsonProperty("reg_locale")
        public String getReg_locale() {
            return reg_locale;
        }

        /**
         * "lang": "en-US"
         */
        @JsonProperty("lang")
        public String getLang() {
            return lang;
        }

        @Override
        public String toString() {
            final ToStringBuilder builder = createToStringBuilder();
            builder.appendSuper(super.toString());
            builder.append("reg_locale", this.reg_locale);
            builder.append("reg_locale", this.reg_locale);

            return builder.toString();
        }
    }

    private Body body;
    private String status;
    private Double time_exec;
    private Date time_server;

    @JsonProperty("body")
    public Body getBody() {
        return this.body;
    }

    public List<Home> getHomes() {
        return this.body.homes;
    }

    public User getUser() {
        return this.body.user;
    }

    /**
     * "status": "ok",
     */
    @JsonProperty("status")
    public String getStatus() {
        return this.status;
    }

    /**
     * "time_exec": 0.019799947738647
     */
    @JsonProperty("time_exec")
    public Double getTime_exec() {
        return this.time_exec;
    }

    /**
     * "time_server": 1441120797
     */
    @JsonProperty("time_server")
    public Date getTime_server() {
        return this.time_server;
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = createToStringBuilder();
        builder.appendSuper(super.toString());
        builder.append("body", this.body);
        builder.append("status", this.status);
        builder.append("time_exec", this.time_exec);
        builder.append("time_server", this.time_server);

        return builder.toString();
    }
}
