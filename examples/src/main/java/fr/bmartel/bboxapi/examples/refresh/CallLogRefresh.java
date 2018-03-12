package fr.bmartel.bboxapi.examples.refresh;

import fr.bmartel.bboxapi.BboxApi;
import fr.bmartel.bboxapi.examples.utils.ExampleUtils;
import fr.bmartel.bboxapi.model.HttpStatus;
import fr.bmartel.bboxapi.model.profile.RefreshAction;
import fr.bmartel.bboxapi.response.ConsumptionResponse;
import fr.bmartel.bboxapi.response.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Profile refresh request example.
 *
 * @author Bertrand Martel
 */
public class CallLogRefresh {

    private final static Logger LOGGER = LogManager.getLogger(CallLogRefresh.class.getName());

    public static void main(String[] args) throws IOException {

        BboxApi api = new BboxApi();

        String pass = ExampleUtils.getPassword();

        api.setPassword(pass);

        long startTime = System.nanoTime();

        ConsumptionResponse consumptionResponse = api.getConsumptionData();

        if (consumptionResponse.getStatus() == HttpStatus.OK) {
            LOGGER.debug("refresh state  : " + consumptionResponse.getProfileList().get(0).getProfile().getState());
            LOGGER.debug("last modified  : " + consumptionResponse.getProfileList().get(0).getProfile()
                    .getChangedDate());
        }

        HttpResponse response = api.refreshProfile(RefreshAction.CALL_LOG);

        LOGGER.debug("refreshProfile status : " + response.getStatus());

        int state = -1;

        while (state != 0) {

            ConsumptionResponse checkResponse = api.getConsumptionData();

            if (checkResponse.getStatus() == HttpStatus.OK) {

                state = checkResponse.getProfileList().get(0).getProfile().getState();

                LOGGER.debug("refresh state  : " + state);
                LOGGER.debug("last modified  : " + checkResponse.getProfileList().get(0).getProfile().getChangedDate());
                if (state != 0) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        state = 0;
                    }
                }
            } else {
                state = 0;
                LOGGER.debug("error profile consumption status : " + consumptionResponse.getStatus());
            }
        }

        long endTime = System.nanoTime();

        LOGGER.debug("duration : " + (endTime - startTime) / 1000000 + "ms");
    }
}
