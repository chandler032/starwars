import {Typography} from "@mui/material";

const ResultTitle = ({ title }) => {
  return (
    <>
      <Typography variant="body1">Searched For </Typography>
      <Typography variant="h4">{title}</Typography>
    </>
  )
}

export default ResultTitle;