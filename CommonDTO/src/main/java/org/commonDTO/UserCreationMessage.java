package org.commonDTO;

import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationMessage {
    private Long id;
    private String username;
    private String password;
    private String authority;
    private boolean isNew;
}