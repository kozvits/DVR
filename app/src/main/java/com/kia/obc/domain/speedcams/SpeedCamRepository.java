package com.kia.obc.domain.speedcams;

import java.util.List;

public interface SpeedCamRepository {
    void loadSignatures(SpeedCamRepository.Callback callback);

    interface Callback {
        void onSuccess(List<SpeedCam> cams);
        void onFailure(Throwable t);
    }
}
