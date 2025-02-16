import {Skeleton, Stack} from "@mui/material";

const ContentSkeleton = () => {
  return (
    <Stack spacing={2}>
      <Skeleton variant="text" sx={{width: "100%", height: "24px"}}/>
      <Skeleton variant="text" sx={{width: "100%", height: "40px"}}/>
      <Skeleton variant="text" sx={{width: "100%", height: "24px"}}/>
      <Skeleton variant="rectangular" sx={{width: "100%", height: "300px"}}/>
    </Stack>
  )
}

export default ContentSkeleton;