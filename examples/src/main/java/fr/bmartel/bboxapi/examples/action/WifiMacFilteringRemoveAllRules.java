/**
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2017 Bertrand Martel
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fr.bmartel.bboxapi.examples.action;

import fr.bmartel.bboxapi.BboxApi;
import fr.bmartel.bboxapi.examples.request.WirelessAclInfo;
import fr.bmartel.bboxapi.examples.utils.ExampleUtils;
import fr.bmartel.bboxapi.model.HttpStatus;
import fr.bmartel.bboxapi.model.wireless.Rules;
import fr.bmartel.bboxapi.response.WirelessAclResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Remove all Wifi MAC filter rules.
 *
 * @author Bertrand Martel
 */
public class WifiMacFilteringRemoveAllRules {

    private final static Logger LOGGER = LogManager.getLogger(WirelessAclInfo.class.getName());

    public static void main(String[] args) {

        BboxApi api = new BboxApi();

        String pass = ExampleUtils.getPassword();

        api.setPassword(pass);

        WirelessAclResponse wirelessResponse = api.getWifiMacFilterInfo();

        if (wirelessResponse.getStatus() == HttpStatus.OK) {

            if (wirelessResponse.getAclResponse().size() > 0) {

                List<Rules> ruleList = wirelessResponse.getAclResponse().get(0).getAclRules().getRuleList();

                for (int i = 0; i < ruleList.size(); i++) {
                    LOGGER.error("delete rule with id " + ruleList.get(i).getId());
                    HttpStatus status = api.deleteMacFilterRule(ruleList.get(i).getId());
                    if (status == HttpStatus.OK) {
                        LOGGER.error("successfully deleted rules : " + ruleList.get(i).getId());
                    } else {
                        LOGGER.error("failed to delete rules : " + ruleList.get(i).getId());
                    }
                }
            } else {
                LOGGER.error("wireless acl rules empty");
            }
        } else {
            LOGGER.error("http error  : " + wirelessResponse.getStatus());
        }
    }
}