@Service
public class ShippingAddressService extends BaseService {
	public MessageResult addAppShippingAddress(String token, String addressName, String addressPhone, String province,
                                               String city, String area, String addressDetail, String isDefault){
        AuthUser authUser = usersService.getAuthUserByToken(token);
        Users user = usersService.findById(authUser.getId());

        int isDefaultInt = NumberUtil.isNumericInt(isDefault) ? Integer.valueOf(isDefault) : 1;
        if(StringUtils.isEmpty(addressName) || StringUtils.isEmpty(addressPhone) || StringUtils.isEmpty(province)
                || StringUtils.isEmpty(city) || StringUtils.isEmpty(area) || StringUtils.isEmpty(addressDetail)){
            return MessageResult.error("请完善信息");
        }
        boolean phone = NumberUtil.isPhone(addressPhone);
        if(!phone) {
            return MessageResult.error("请填入正确的手机号");
        }

        ShippingAddress shippingAddress = shippingAddressDao.getShippingAddressLastByUserId(user.getId());
        if (null == shippingAddress) {
            shippingAddress = new ShippingAddress(user,addressName,addressPhone,province,city,area, addressDetail,isDefaultInt);
        } else {
            shippingAddress.setUsers(user);
            shippingAddress.setAddressName(addressName);
            shippingAddress.setAddressPhone(addressPhone);
            shippingAddress.setProvince(province);
            shippingAddress.setCity(city);
            shippingAddress.setArea(area);
            shippingAddress.setAddressDetail(addressDetail);
            shippingAddress.setIsDefault(isDefaultInt);
        }
        shippingAddressDao.save(shippingAddress);

        MessageResult findAppShippingAddress = findAppShippingAddress(token);
        Object data = findAppShippingAddress.getData();
        MessageResult result = MessageResult.success();
        result.setData(data);
        return result;
    }
}