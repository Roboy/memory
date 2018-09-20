package org.roboy.memory.util;

import java.util.Arrays;
import java.util.HashSet;

@Deprecated
public class Dictionary {
    public static final HashSet<String> LABEL_VALUES = new HashSet<String>(Arrays.asList(
            "Person",
            "Telegram_person",
            "Facebook_person",
            "Slack_person",
            "Robot",
            "Company",
            "University",
            "City",
            "Country",
            "Hobby",
            "Occupation",
            "Object",
            "Location",
            "Organization"));
    public static final HashSet<String> RELATIONSHIP_VALUES = new HashSet<String> (Arrays.asList(
            "FRIEND_OF",
            "LIVE_IN",
            "FROM",
            "WORK_FOR",
            "STUDY_AT",
            "MEMBER_OF",
            "HAS_HOBBY",
            "KNOW",
            "IS",
            "PART_OF",
            "IS_IN",
            "OCCUPIED_AS",
            "CHILD_OF",
            "SIBLING_OF"));
}
