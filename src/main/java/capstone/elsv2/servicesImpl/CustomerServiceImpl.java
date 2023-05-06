package capstone.elsv2.servicesImpl;

import capstone.elsv2.dto.common.OrderChatDTO;
import capstone.elsv2.dto.common.PageDTO;
import capstone.elsv2.dto.common.StatisticsDTO;
import capstone.elsv2.dto.customer.*;
import capstone.elsv2.emunCode.StatusCode;
import capstone.elsv2.entities.Account;
import capstone.elsv2.entities.Booking;
import capstone.elsv2.entities.CustomerProfile;
import capstone.elsv2.entities.Wallet;
import capstone.elsv2.repositories.*;
import capstone.elsv2.services.BookingService;
import capstone.elsv2.services.CusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomerServiceImpl implements CusService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BookingServiceImpl bookingService;

    @Autowired
    private CustomerProfileRepository customerProfileRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public PageDTO findAllCustomer(int pageNumber, int pageSize) {
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<CustomerProfile> customerPage = customerProfileRepository.findAll(pageable);
        List<CustomerProfile> customers = customerPage.getContent();
        for (CustomerProfile customer : customers) {
            CustomerDTO customerDTO = CustomerDTO.builder()
                    .id(customer.getId())
                    .fullName(customer.getAccount().getFullName())
                    .phone(customer.getAccount().getPhone())
                    .email(customer.getAccount().getEmail())
                    .address(customer.getAddress())
                    .gender(customer.getGender())
                    .status(customer.getAccount().getStatus())
                    .build();
            customerDTOS.add(customerDTO);
        }
        int offset = customerPage.getNumber() * customerPage.getSize() + 1;
        PageDTO pageDTO = PageDTO.builder()
                .data(customerDTOS)
                .pageSize(customerPage.getSize())
                .hasNextPage(customerPage.hasNext())
                .pageIndex(customerPage.getNumber() + 1)
                .totalRecord(customerPage.getTotalElements())
                .fromRecord(offset)
                .toRecord(offset + customerPage.getNumberOfElements() - 1)
                .hasPreviousPage(customerPage.hasPrevious())
                .totalPages(customerPage.getTotalPages())
                .build();

        return pageDTO;
    }

    @Override
    public CustomerDetailDTO findById(String id) {
        CustomerDetailDTO customerDetailDTO = null;

        Account customer = accountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy khách hàng nào cả"));
        customerDetailDTO = convertEntityToDTO(customer);
        return customerDetailDTO;
    }

    private CustomerDetailDTO convertEntityToDTO(Account customer) {
        if(customer.getCustomerProfile().getAddress().equals("")){
            customer.getCustomerProfile().setAddress(" ; ");
        }
        String address[] = customer.getCustomerProfile().getAddress().split(";");
        String _address = address[0];

        Double latitude = 0D;
        Double longitude = 0D;
        String zone = "";
        if(address.length ==4){
            latitude = Double.parseDouble(address[1]);
            longitude = Double.parseDouble(address[2]);
            zone = address[3];
        }
        CustomerDetailDTO customerDetailDTO = CustomerDetailDTO.builder()
                .id(customer.getId())
                .fullName(customer.getFullName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .address(_address)
                .avatar(customer.getCustomerProfile().getAvatarImgUrl())
                .gender(customer.getCustomerProfile().getGender())
                .status(customer.getStatus())
                .zone(zone)
                .latitude(latitude)
                .longitude(longitude)
                .dob(customer.getCustomerProfile().getDob())
                .createDate(customer.getCreateDate())
                .description(customer.getCustomerProfile().getDescription())
                .idCardNumber(customer.getCustomerProfile().getIdCardNumber())
                .build();
        return customerDetailDTO;
    }

    @Override
    public Boolean updateCustomer(UpdateCustomerDTO updateCustomerDTO) {
        Boolean isUpdate = false;
        Account accountCustomer = accountRepository.findById(updateCustomerDTO.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy khách hàng nào cả"));
        try {
            String address = updateCustomerDTO.getAddress()+";"+updateCustomerDTO.getLatitude()+";"+updateCustomerDTO.getLongitude()+";"+updateCustomerDTO.getDistrict();
            accountCustomer.setFullName(updateCustomerDTO.getFullName());
            accountCustomer.setPhone(updateCustomerDTO.getPhone());
            accountCustomer = accountRepository.save(accountCustomer);
            CustomerProfile customerProfile = customerProfileRepository.findById(updateCustomerDTO.getId()).get();
            customerProfile.setAddress(address);
            customerProfile.setAvatarImgUrl(updateCustomerDTO.getAvatarImgUrl());
            customerProfile.setGender(updateCustomerDTO.getGender());
            customerProfile.setDob(updateCustomerDTO.getDob());
            customerProfile.setDescription(updateCustomerDTO.getDescription());
            customerProfile.setIdCardNumber(updateCustomerDTO.getIdCardNumber());
            customerProfile.setAccount(accountCustomer);
            customerProfileRepository.save(customerProfile);
            isUpdate = true;

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "không thể thay đổi được thông tin");
        }
        return isUpdate;
    }

    @Override
    public Boolean banCustomer(String id) {
        Boolean isBan = false;
        Account customer = accountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy khách hàng nào cả"));
        if (customer.getStatus().equals(StatusCode.ACTIVATE.toString())) {
            customer.setStatus(StatusCode.DEACTIVATE.toString());
            bookingService.cancelAllBookingCustomer(customer.getId());
            accountRepository.save(customer);
            isBan = true;
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Khánh hàng này đã bị khóa không thể khóa nữa");
        }
        return isBan;
    }



    @Override
    public Boolean unBanCustomer(String id) {
        Boolean isUnBan = false;
        Account customer = accountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.valueOf(404), "Không tìm thấy khách hàng nào cả"));
        if (customer.getStatus().equals(StatusCode.DEACTIVATE.toString())) {
            customer.setStatus(StatusCode.ACTIVATE.toString());
            accountRepository.save(customer);
            isUnBan = true;
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Khánh hàng này đã được kích hoạt không thể kích hoạt nữa");
        }

        return isUnBan;
    }

    @Override
    public Boolean register(CustomerRegisterDTO customerRegisterDTO) {
        boolean isRegister = false;
        if (accountRepository.findByEmail(customerRegisterDTO.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.valueOf(409), "Email đã được sử dụng");
        }
        if (accountRepository.findByPhone(customerRegisterDTO.getPhone()) != null) {
            throw new ResponseStatusException(HttpStatus.valueOf(409), "Số điện thoại đã được sử dụng");
        }
        try {
            Account user = Account.builder()
                    .email(customerRegisterDTO.getEmail())
                    .fullName(customerRegisterDTO.getName())
                    .phone(customerRegisterDTO.getPhone())
                    .status(StatusCode.ACTIVATE.toString())
                    .createDate(LocalDate.now())
                    .role(roleRepository.findAllByName("CUSTOMER"))
                    .password(passwordEncoder.encode(customerRegisterDTO.getPassword()))
                    .build();
            user = accountRepository.save(user);
            CustomerProfile customerProfile = CustomerProfile.builder()
                    .account(user)
                    .address("")
                    .build();
            customerProfileRepository.save(customerProfile);

            Wallet wallet = Wallet.builder()
                    .account(user)
                    .amount(BigDecimal.valueOf(0L))
                    .build();
            walletRepository.save(wallet);

            isRegister = true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.valueOf(400), "Tài khoản đăng kí thất bại");
        }
        return isRegister;
    }

    @Override
    public PageDTO searchByKeyWord(String keyWord, int pageNumber, int pageSize) {
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<CustomerProfile> customerPage = customerProfileRepository.findAll(pageable);
        List<CustomerProfile> customers = customerPage.getContent();
        if (keyWord.equals("")) {
            for (CustomerProfile customer : customers) {
                CustomerDTO customerDTO = CustomerDTO.builder()
                        .id(customer.getId())
                        .fullName(customer.getAccount().getFullName())
                        .phone(customer.getAccount().getPhone())
                        .email(customer.getAccount().getEmail())
                        .address(customer.getAddress())
                        .gender(customer.getGender())
                        .status(customer.getAccount().getStatus())
                        .build();
                customerDTOS.add(customerDTO);
            }
        }else {
            for (CustomerProfile customer : customers) {
                if (customer.getAccount().getFullName().contains(keyWord)
                        || customer.getAccount().getPhone().contains(keyWord)
                        || customer.getAccount().getStatus().equals(keyWord)
                        || customer.getAccount().getEmail().contains(keyWord)
                        || customer.getAddress().contains(keyWord)
                        || customer.getGender().equals(keyWord)) {
                    CustomerDTO customerDTO = CustomerDTO.builder()
                            .id(customer.getId())
                            .fullName(customer.getAccount().getFullName())
                            .phone(customer.getAccount().getPhone())
                            .email(customer.getAccount().getEmail())
                            .address(customer.getAddress())
                            .gender(customer.getGender())
                            .status(customer.getAccount().getStatus())
                            .build();
                    customerDTOS.add(customerDTO);
                }
            }
        }
            PageDTO pageDTO = PageDTO.builder()
                    .data(customerDTOS)
                    .pageSize(customerPage.getSize())
                    .hasNextPage(customerPage.hasNext())
                    .pageIndex(customerPage.getNumber() + 1)
                    .hasPreviousPage(customerPage.hasPrevious())
                    .totalPages(customerPage.getTotalPages())
                    .build();

            return pageDTO;

        }

//        @Override
//        public StatisticsDTO countAllCus () {
//            StatisticsDTO totalCusDTO = new StatisticsDTO();
//            totalCusDTO.setTotal(customerProfileRepository.findAll().size());
//            return totalCusDTO;
//        }

//    @Override
//    public Set<OrderChatDTO> getAllMessageByCustomerId(String customerId) {
//        List<Booking> bookings = bookingRepository.findAllByCustomer_Id(customerId);
//        Set<OrderChatDTO> orderChatDTOS = new HashSet<>();
//        for (Booking booking: bookings) {
//            if(StatusCode.ACTIVATE.toString().equals(booking.getStatus()) || StatusCode.COMPLETED.toString().equals(booking.getStatus())) {
//                OrderChatDTO orderChatDTO = OrderChatDTO.builder()
//                        .otherId(booking.getSitter().getId())
//                        .otherAvatar(booking.getSitter().getAvatarImgUrl())
//                        .otherEmail(booking.getSitter().getAccount().getEmail())
//                        .otherName(booking.getSitter().getAccount().getFullName())
//                        .build();
//                orderChatDTOS.add(orderChatDTO);
//            }
//        }
//        return orderChatDTOS;
//    }
}
