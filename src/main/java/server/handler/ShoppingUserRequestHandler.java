package server.handler;

import org.json.JSONArray;
import org.json.JSONObject;
import server.service.ShoppingUserService;

public class ShoppingUserRequestHandler implements RequestHandler {

    @Override
    public String handle(JSONObject parameters) {
        ShoppingUserService userService = new ShoppingUserService();
        JSONObject response = new JSONObject();

        // 获取操作类型
        String action = parameters.optString("action", "viewUser");

        switch (action) {
            case "viewUser":  // 查看用户的所有地址和电话
                String username = parameters.optString("username", null);
                if (username == null || username.isEmpty()) {
                    response.put("status", "fail").put("message", "缺少用户名");
                } else {
                    response = userService.getUserDetails(username);
                }
                break;

            case "updateUserContacts":  // 更新用户的所有地址和电话
                String updateUsername = parameters.optString("username", null);
                JSONArray addressesArray = parameters.optJSONArray("addresses");
                JSONArray telephonesArray = parameters.optJSONArray("telephones");

                if (updateUsername == null || updateUsername.isEmpty() || addressesArray == null || telephonesArray == null) {
                    response.put("status", "fail").put("message", "缺少必要的参数");
                } else {
                    String[] addresses = new String[addressesArray.length()];
                    String[] telephones = new String[telephonesArray.length()];

                    for (int i = 0; i < addressesArray.length(); i++) {
                        addresses[i] = addressesArray.getString(i);
                    }
                    for (int i = 0; i < telephonesArray.length(); i++) {
                        telephones[i] = telephonesArray.getString(i);
                    }

                    response = userService.updateUserContacts(updateUsername, addresses, telephones);
                }
                break;

            case "updateUserContactAtIndex":  // 更新用户的某个地址和电话（指定索引）
                String usernameAtIndex = parameters.optString("username", null);
                int index = parameters.optInt("index", -1);
                String newAddress = parameters.optString("newAddress", null);
                String newTelephone = parameters.optString("newTelephone", null);

                if (usernameAtIndex == null || usernameAtIndex.isEmpty() || newAddress == null || newTelephone == null || index < 0) {
                    response.put("status", "fail").put("message", "缺少必要的参数或索引无效");
                } else {
                    response = userService.updateUserContactAtIndex(usernameAtIndex, index, newAddress, newTelephone);
                }
                break;

            case "addUserContact":  // 添加用户新的地址和电话
                String addUsername = parameters.optString("username", null);
                String newAddr = parameters.optString("address", null);
                String newTel = parameters.optString("telephone", null);

                if (addUsername == null || addUsername.isEmpty() || newAddr == null || newTel == null) {
                    response.put("status", "fail").put("message", "缺少必要的参数");
                } else {
                    response = userService.addUserContact(addUsername, newAddr, newTel);
                }
                break;

            case "deleteUserContact":  // 删除用户的某个地址和电话
                String delUsername = parameters.optString("username", null);
                int delIndex = parameters.optInt("index", -1);  // 要删除的地址和电话的索引

                if (delUsername == null || delUsername.isEmpty() || delIndex < 0) {
                    response.put("status", "fail").put("message", "缺少必要的参数或索引无效");
                } else {
                    response = userService.deleteUserContact(delUsername, delIndex);
                }
                break;

            default:
                response.put("status", "fail").put("message", "未知的操作类型");
                break;
        }

        return response.toString();
    }
}
