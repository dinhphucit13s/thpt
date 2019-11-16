package fpt.dps.dtms.service.mapper.msc;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fpt.dps.dtms.domain.User;
import fpt.dps.dtms.service.dto.msc.UserMSCDTO;

@Service
public class UserMSCMapper {
	public UserMSCDTO userToUserMSCDTO(User user) {
        return new UserMSCDTO(user);
    }

    public List<UserMSCDTO> usersToUserMSCDTOs(List<User> users) {
        return users.stream()
            .filter(Objects::nonNull)
            .map(this::userToUserMSCDTO)
            .collect(Collectors.toList());
    }
}
