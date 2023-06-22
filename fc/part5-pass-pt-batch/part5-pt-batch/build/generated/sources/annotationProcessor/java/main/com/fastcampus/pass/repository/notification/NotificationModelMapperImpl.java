package com.fastcampus.pass.repository.notification;

import com.fastcampus.pass.repository.booking.BookingEntity;
import com.fastcampus.pass.repository.user.UserEntity;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-22T17:18:44+0900",
    comments = "version: 1.5.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.5.jar, environment: Java 17.0.5 (Amazon.com Inc.)"
)
public class NotificationModelMapperImpl implements NotificationModelMapper {

    @Override
    public NotificationEntity toNotificationEntity(BookingEntity bookingEntity, NotificationEvent event) {
        if ( bookingEntity == null && event == null ) {
            return null;
        }

        NotificationEntity notificationEntity = new NotificationEntity();

        if ( bookingEntity != null ) {
            notificationEntity.setUuid( bookingEntityUserEntityUserId( bookingEntity ) );
            notificationEntity.setText( text( bookingEntity.getStartedAt() ) );
        }
        notificationEntity.setEvent( event );

        return notificationEntity;
    }

    private String bookingEntityUserEntityUserId(BookingEntity bookingEntity) {
        if ( bookingEntity == null ) {
            return null;
        }
        UserEntity userEntity = bookingEntity.getUserEntity();
        if ( userEntity == null ) {
            return null;
        }
        String userId = userEntity.getUserId();
        if ( userId == null ) {
            return null;
        }
        return userId;
    }
}
