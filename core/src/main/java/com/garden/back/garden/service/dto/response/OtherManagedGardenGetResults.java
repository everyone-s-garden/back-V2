package com.garden.back.garden.service.dto.response;

import java.util.List;

public record OtherManagedGardenGetResults(
    List<OtherManagedGardenGetResult> otherManagedGardenGetResponse,
    Long nextManagedGardenId,
    boolean hasNext
) {
    public static OtherManagedGardenGetResults to(MyManagedGardenGetResults responses) {
        return new OtherManagedGardenGetResults(
            responses.myManagedGardenGetResponse().stream()
                .map(OtherManagedGardenGetResult::to).toList(),
            responses.nextManagedGardenId(),
            responses.hasNext()
        );
    }

    public record OtherManagedGardenGetResult(
        Long myManagedGardenId,
        String myManagedGardenName,
        String createdAt,
        String image,
        String description
    ) {

        public static OtherManagedGardenGetResult to(
            MyManagedGardenGetResults.MyManagedGardenGetResult response) {
            return new OtherManagedGardenGetResult(
                response.myManagedGardenId(),
                response.myManagedGardenName(),
                response.createdAt(),
                response.images(),
                response.description()
            );
        }

    }

}
