package org.roboy.memory.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Dictionary {
    public static final HashSet<String> LABEL_VALUES = new HashSet<String>(Arrays.asList("Person","Robot","Company","University","City","Country","Hobby","Occupation","Object"));
    public static final HashSet<String> RELATION_VALUES = new HashSet<String> (Arrays.asList("FRIEND_OF","LIVE_IN","FROM","WORK_FOR","STUDY_AT","MEMBER_OF","HAS_HOBBY","KNOW","IS","PART_OF","IS_IN" ));
}
