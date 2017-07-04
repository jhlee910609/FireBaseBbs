package com.junhee.android.firebase_advanced.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JunHee on 2017. 7. 4..
 */

public class Data {
    // 공용으로 사용되는 데이터 저장소
    // 모든 Activity에서 접근할 수 있음
    public static List<Bbs> list = new ArrayList<>();
}
