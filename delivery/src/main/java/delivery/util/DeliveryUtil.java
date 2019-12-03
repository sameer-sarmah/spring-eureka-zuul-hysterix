package delivery.util;

import delivery.entities.Address;

public class DeliveryUtil {
   public static Address convertAddressModelToAddressEntity(core.models.Address address){
        Address entity = new Address();
        entity.setAddress(address.getAddress());
        entity.setCity(address.getCity());
        entity.setCountry(address.getCountry());
        entity.setPhone(address.getPhone());
        entity.setState(address.getState());
        entity.setZip(address.getZip());
        return entity;
    }

    public static core.models.Address convertAddressEntityToAddressModel(Address entity){
        core.models.Address address = new core.models.Address();
        address.setAddress(entity.getAddress());
        address.setCity(entity.getCity());
        address.setCountry(entity.getCountry());
        address.setPhone(entity.getPhone());
        address.setState(entity.getState());
        address.setZip(entity.getZip());
        return address;
    }
}
