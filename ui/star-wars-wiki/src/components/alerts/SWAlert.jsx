import {Alert, Stack} from "@mui/material";

const SWAlert = ({alertType, message}) => {
  return (
    <Stack sx={{width: "100%"}} spacing={2}>
      <Alert severity={alertType}>{message}</Alert>
    </Stack>
  )
}

export default SWAlert;