package lk.rc.aws.awsinstagramclone.api.service.impl;

import lk.rc.aws.awsinstagramclone.api.dao.FollowRepository;
import lk.rc.aws.awsinstagramclone.api.dao.ProfileDetailRepository;
import lk.rc.aws.awsinstagramclone.api.dto.FollowResponseBean;
import lk.rc.aws.awsinstagramclone.api.dto.ProfileDTO;
import lk.rc.aws.awsinstagramclone.api.service.FollowService;
import lk.rc.aws.awsinstagramclone.model.Follow;
import lk.rc.aws.awsinstagramclone.model.ProfileDetails;
import lk.rc.aws.awsinstagramclone.util.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private ProfileDetailRepository profileDetailRepository;

    @Override
    @Transactional
    public FollowResponseBean followProfile(ProfileDetails profile, int followProfileId) throws Exception {
        FollowResponseBean responseBean = new FollowResponseBean();

        ProfileDetails followProfile = profileDetailRepository.getProfileDetailsByProfileId(followProfileId);
        if (followProfile != null) {
            Follow follow = new Follow();
            follow.setProfileId(profile);
            follow.setFollowingProfileId(followProfile);
            follow.setFollowedDate(new Timestamp(System.currentTimeMillis()));
            follow.setStatus("ACT");

            follow = followRepository.save(follow);
            responseBean.setResponseCode(ResponseCode.SUCCESS);
            responseBean.setResponseMsg("");
        }else {

            responseBean.setResponseCode(ResponseCode.FAILED);
            responseBean.setResponseMsg("No Profile found to the Profile Id - " + followProfileId);
        }

        return responseBean;
    }

    @Override
    public FollowResponseBean getFollowers(ProfileDetails profile) throws Exception {
        FollowResponseBean responseBean = new FollowResponseBean();
        List<ProfileDTO> followerList = new ArrayList<>();

        List<ProfileDetails> followers = followRepository.getProfileWhichFollowMe(profile.getProfileId());

        for (ProfileDetails profileId : followers) {
//            ProfileDetails profileId = follow.getProfileId();
            ProfileDTO details = new ProfileDTO();

            details.setProfileId(profileId.getProfileId());
            details.setFullName(profileId.getFullName());
            details.setDateOfBirth(profileId.getDateOfBirth());
            details.setStatus(profileId.getStatus());
            details.setFirstName(profileId.getFirstName());
            details.setLastName(profileId.getLastName());
            details.setAge(profileId.getAge());

            followerList.add(details);
        }

        responseBean.setFollowersList(followerList);
        responseBean.setResponseCode(ResponseCode.SUCCESS);
        responseBean.setResponseMsg("");
        return responseBean;
    }

    @Override
    public FollowResponseBean getFollowings(ProfileDetails profile) throws Exception {
        FollowResponseBean responseBean = new FollowResponseBean();
        List<ProfileDTO> followingList = new ArrayList<>();

//        List<Follow> followings = followRepository.getAllFollowsByProfileId(profile);
        List<ProfileDetails> followings = followRepository.getProfileWhichFollowedByMe(profile.getProfileId());

        for (ProfileDetails profileId : followings) {
//            ProfileDetails profileId = follow.getFollowingProfileId();

            ProfileDTO details = new ProfileDTO();

            details.setProfileId(profileId.getProfileId());
            details.setFullName(profileId.getFullName());
            details.setDateOfBirth(profileId.getDateOfBirth());
            details.setStatus(profileId.getStatus());
            details.setFirstName(profileId.getFirstName());
            details.setLastName(profileId.getLastName());
            details.setAge(profileId.getAge());


            followingList.add(details);
        }

        responseBean.setFollowingList(followingList);
        responseBean.setResponseCode(ResponseCode.SUCCESS);
        responseBean.setResponseMsg("");
        return responseBean;
    }
}
