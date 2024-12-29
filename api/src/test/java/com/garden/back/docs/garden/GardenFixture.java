package com.garden.back.docs.garden;

import com.garden.back.garden.controller.dto.request.*;
import com.garden.back.garden.domain.vo.GardenStatus;
import com.garden.back.garden.domain.vo.GardenType;
import com.garden.back.garden.facade.dto.GardenDetailFacadeResponse;
import com.garden.back.garden.facade.dto.OtherGardenGetFacadeResponses;
import com.garden.back.garden.service.dto.response.*;

import java.util.List;
import java.util.Set;

public class GardenFixture {
    public static GardenByNameResults gardenByNameResults() {
        List<GardenByNameResults.GardenByNameResult> gardensByName = List.of(
            new GardenByNameResults.GardenByNameResult(1L, "도연이네 텃밭", "제주도 서귀포"),
            new GardenByNameResults.GardenByNameResult(2L, "도연이네 주말농장", "경기도 수정시 분당구")
        );

        return new GardenByNameResults(gardensByName, false);
    }

    public static GardenAllResults gardenAllResults() {
        List<GardenAllResults.GardenAllResult> gardenAllResults = List.of(
            new GardenAllResults.GardenAllResult(
                1L,
                37.4449168,
                127.1388684,
                "별이네 텃밭",
                GardenType.PUBLIC.name(),
                "100000",
                "100",
                GardenStatus.ACTIVE.name(),
                List.of("www.garden.com")
            )
        );

        return new GardenAllResults(gardenAllResults, false);
    }

    public static GardenByComplexesWithScrollResults gardenByComplexesWithScrollResults() {
        Set<GardenByComplexesWithScrollResults.GardenByComplexesWithScrollResult> gardenByComplexesWithScrollResults =
            Set.of(new GardenByComplexesWithScrollResults.GardenByComplexesWithScrollResult(
                    1L,
                    "100",
                    "별이네 텃밭",
                    "100000",
                    List.of("www.garden.com"),
                    GardenStatus.ACTIVE.name(),
                    GardenType.PUBLIC.name(),
                    37.4449168,
                    127.1388684
                )
            );

        return new GardenByComplexesWithScrollResults(gardenByComplexesWithScrollResults, false);
    }

    public static GardenByComplexesResults gardenByComplexesResults() {
        Set<GardenByComplexesResults.GardenByComplexesResult> gardenByComplexesResults =
            Set.of(new GardenByComplexesResults.GardenByComplexesResult(
                    1L,
                    "100",
                    "별이네 텃밭",
                    "100000",
                    List.of("www.garden.com"),
                    GardenStatus.ACTIVE.name(),
                    GardenType.PUBLIC.name(),
                    37.4449168,
                    127.1388684
                )
            );

        return new GardenByComplexesResults(gardenByComplexesResults);
    }

    public static GardenDetailFacadeResponse gardenDetailResult() {
        return new GardenDetailFacadeResponse(
            1L,
            "인천광역시 서구 신현동 222-22",
            37.4449168,
            127.1388684,
            "진겸이네 주말농장",
            GardenType.PUBLIC.name(),
            "100000",
            "000-0000-0000",
            "1000",
            GardenStatus.ACTIVE.name(),
            1L,
            "2023.12.01",
            "2023.12.25",
            "농기구를 빌릴 수 있는 자판기가 있습니다. 작물 키우는 법도 알려드려요",
            List.of("www.garden.com"),
            "화장실 등",
            1L,
            1L,
            "4567"
        );
    }

    public static RecentGardenResults recentGardenResults() {
        return new RecentGardenResults(
            List.of(
                new RecentGardenResults.RecentGardenResult(
                    1L,
                    34.123,
                    127.123,
                    "1000",
                    "영수네 텃밭",
                    "100000",
                    List.of("www.garden.com"),
                    GardenStatus.ACTIVE.name(),
                    GardenType.PUBLIC.name()
                )
            )
        );
    }

    public static GardenMineResults gardenMineResults() {
        return new GardenMineResults(
            List.of(
                new GardenMineResults.GardenMineResult(
                    1L,
                    "1000",
                    "영수네 텃밭",
                    "100000",
                    GardenStatus.ACTIVE.name(),
                    List.of("www.garden.com")
                )
            ),
            2L,
            false
        );
    }

    public static GardenLikeByMemberResults gardenLikeByMemberResults() {
        return new GardenLikeByMemberResults(
            List.of(
                new GardenLikeByMemberResults.GardenLikeByMemberResult(
                    1L,
                    "1000",
                    "영수네 텃밭",
                    "100000",
                    GardenStatus.ACTIVE.name(),
                    List.of("www.garden.com")
                )
            ),
            0L,
            false
        );
    }

    public static GardenLikeCreateRequest gardenLikeCreateRequest() {
        return new GardenLikeCreateRequest(1L);
    }

    public static GardenLikeDeleteRequest gardenLikeDeleteRequest() {
        return new GardenLikeDeleteRequest(
            1L
        );
    }

    public static GardenCreateRequest gardenCreateRequest() {
        return new GardenCreateRequest(
            "별이네 텃밭",
            "100",
            "200",
            "ACTIVE",
            "000-000-0000",
            "인천광역시 서구 만수동 200",
            37.444917,
            127.138868,
            true,
            true,
            true,
            "화장실이 깨끗하고 토양의 질이 좋습니다. 모두 놀러오세요",
            "2023.12.01",
            "2023.12.23"
        );
    }

    public static GardenUpdateRequest gardenUpdateRequest() {
        return new GardenUpdateRequest(
            List.of("https://kr.object.ncloudstorage.com/every-garden/images/garden/background.jpg"),
            "별이네 텃밭",
            "100",
            "200",
            "ACTIVE",
            "PRIVATE",
            "000-000-0000",
            "인천광역시 서구 만수동 200",
            37.444917,
            127.138868,
            true,
            true,
            true,
            "화장실이 깨끗하고 흙이 좋아요",
            "2023.12.01",
            "2023.12.23"
        );
    }

    public static MyManagedGardenGetResults myManagedGardenGetResults() {
        return new MyManagedGardenGetResults(
            List.of(
                new MyManagedGardenGetResults.MyManagedGardenGetResult(
                    "별이네 주말농장",
                    1L,
                    "2023.12.01",
                    "https://kr.object.ncloudstorage.com/every-garden/images/garden/background.jpg",
                    "이번 겨울에 고구마를 심었더니 이렇게 많이 열렸어요!"
                )
            ),
            0L,
            false
        );
    }

    public static MyManagedGardenCreateRequest myManagedGardenCreateRequest() {
        return new MyManagedGardenCreateRequest(
            "양평텃밭",
            "2023.12.01",
            "이번 겨울에 고구마를 심었더니 이렇게 많이 열렸어요!"
        );
    }

    public static MyManagedGardenUpdateRequest myManagedGardenUpdateRequest() {
        return new MyManagedGardenUpdateRequest(
            "김별텃밭",
            "2023.12.01",
            "이번 겨울에 고구마를 심었더니 이렇게 많이 열렸어요!"
        );
    }

    public static MyManagedGardenDetailResult myManagedGardenDetailResult() {
        return new MyManagedGardenDetailResult(
            1L,
            "금쪽이네 텃밭",
            "2023.12.01",
            "https://kr.object.ncloudstorage.com/every-garden/images/garden/background.jpg",
            "이번 겨울에 고구마를 심었더니 이렇게 많이 열렸어요!"
        );
    }

    public static RecentCreatedGardenResults recentCreatedGardenResults() {
        return new RecentCreatedGardenResults(
            List.of(
                new RecentCreatedGardenResults.RecentCreatedGardenResult(
                    1L,
                    "건우네 주말농장",
                    "경기도 오산시 오산동 22",
                    37.444917,
                    127.138868,
                    "2023.12.01",
                    "2023.12.31",
                    "20000",
                    false,
                    "https://kr.object.ncloudstorage.com/every-garden/images/garden/background.jpg"
                )
            )
        );
    }

    public static GardenLocationResult gardenLocationResult() {
        return new GardenLocationResult(
            34.123,
            127.123
        );
    }

    public static OtherManagedGardenGetResults otherManagedGardenGetResults() {
        return new OtherManagedGardenGetResults(
            List.of(
                new OtherManagedGardenGetResults.OtherManagedGardenGetResult(
                    1L,
                    "별이네 주말농장",
                    "2023.12.01",
                    "https://kr.object.ncloudstorage.com/every-garden/images/garden/background.jpg",
                    "이번 겨울에 고구마를 심었더니 이렇게 많이 열렸어요!"
                )
            ),
            0L,
            false
        );
    }

    public static OtherGardenGetFacadeResponses otherGardenGetFacadeResponses() {
        return new OtherGardenGetFacadeResponses(
            List.of(
                new OtherGardenGetFacadeResponses.OtherGardenGetFacadeResponse(
                    1L,
                    "별이네 주말농장",
                    "2023.12.01",
                    "010-0000-0000",
                    GardenStatus.ACTIVE,
                    List.of("https://kr.object.ncloudstorage.com/every-garden/images/garden/background.jpg"),
                    1L,
                    false
                )
            ),
            0L,
            false
        );
    }
}
