package com.wardzionn.githubproxy.mapper;

import com.wardzionn.githubproxy.dto.DoctorDto;
import com.wardzionn.githubproxy.dto.responses.AccountResponseDto;
import com.wardzionn.githubproxy.dto.responses.DoctorResponseDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-21T21:26:44+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.5 (Oracle Corporation)"
)
@Component
public class DoctorDtoMapperImpl implements DoctorDtoMapper {

    @Override
    public DoctorDto toDoctorDto(DoctorResponseDto doctorResponseDto) {
        if ( doctorResponseDto == null ) {
            return null;
        }

        DoctorDto.DoctorDtoBuilder doctorDto = DoctorDto.builder();

        doctorDto.firstName( doctorResponseDtoAccountDtoFirstName( doctorResponseDto ) );
        doctorDto.lastName( doctorResponseDtoAccountDtoLastName( doctorResponseDto ) );
        doctorDto.email( doctorResponseDtoAccountDtoEmail( doctorResponseDto ) );
        doctorDto.id( doctorResponseDto.getId() );
        doctorDto.specialization( doctorResponseDto.getSpecialization() );

        return doctorDto.build();
    }

    @Override
    public List<DoctorDto> toDoctorDtos(List<DoctorResponseDto> doctorResponseDtos) {
        if ( doctorResponseDtos == null ) {
            return null;
        }

        List<DoctorDto> list = new ArrayList<DoctorDto>( doctorResponseDtos.size() );
        for ( DoctorResponseDto doctorResponseDto : doctorResponseDtos ) {
            list.add( toDoctorDto( doctorResponseDto ) );
        }

        return list;
    }

    private String doctorResponseDtoAccountDtoFirstName(DoctorResponseDto doctorResponseDto) {
        AccountResponseDto accountDto = doctorResponseDto.getAccountDto();
        if ( accountDto == null ) {
            return null;
        }
        return accountDto.getFirstName();
    }

    private String doctorResponseDtoAccountDtoLastName(DoctorResponseDto doctorResponseDto) {
        AccountResponseDto accountDto = doctorResponseDto.getAccountDto();
        if ( accountDto == null ) {
            return null;
        }
        return accountDto.getLastName();
    }

    private String doctorResponseDtoAccountDtoEmail(DoctorResponseDto doctorResponseDto) {
        AccountResponseDto accountDto = doctorResponseDto.getAccountDto();
        if ( accountDto == null ) {
            return null;
        }
        return accountDto.getEmail();
    }
}
