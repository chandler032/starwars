import {Paper, styled, Typography} from "@mui/material";

const AppearancePaper = styled(Paper)(({theme}) => ({
  display: "flex",
  flexDirection: "column",
  marginRight: theme.spacing(2),
  padding: theme.spacing(2),
  width: "128px",
  height: "128px",
  backgroundColor: theme.palette.grey[300],
}))

const AppearanceCaption = styled(Typography)(({theme}) => ({
  fontWeight: "bold",
  color: theme.palette.grey[800]
}))

const AppearanceCount = styled(Typography)(({theme}) => ({
  color: theme.palette.grey[800]
}))

const AppearanceStat = ({ caption, count }) => {
  return (
    <AppearancePaper>
      <AppearanceCount variant="h2">{count}</AppearanceCount>
      <AppearanceCaption variant="caption">{caption}</AppearanceCaption>
    </AppearancePaper>
  )
}

export default AppearanceStat;